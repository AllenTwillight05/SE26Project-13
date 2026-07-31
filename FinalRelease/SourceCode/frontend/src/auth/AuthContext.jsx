import { createContext, useCallback, useContext, useEffect, useMemo, useState } from "react";
import { clearStoredAuth, getStoredAuth, setStoredAuth } from "../services/authStorage";
import { useAppServices } from "../services/ServiceContext";

const AuthContext = createContext(null);

function isUnauthorizedError(error) {
  return error?.status === 401 || error?.status === 403;
}

export function AuthProvider({ children }) {
  const { auth } = useAppServices();
  const storedAuth = useMemo(() => getStoredAuth(), []);
  const [token, setToken] = useState(storedAuth.token);
  const [user, setUser] = useState(storedAuth.user);
  const [loading, setLoading] = useState(Boolean(storedAuth.token));

  const clearAuthState = useCallback(() => {
    clearStoredAuth();
    setToken(null);
    setUser(null);
  }, []);

  const persistAuthState = useCallback((authResponse) => {
    setStoredAuth(authResponse);
    setToken(authResponse.token);
    setUser(authResponse.user);
    return authResponse;
  }, []);

  const refreshCurrentUser = useCallback(async () => {
    const { token: currentToken } = getStoredAuth();

    if (!currentToken) {
      clearAuthState();
      return null;
    }

    try {
      const currentUser = await auth.me();
      setStoredAuth({ token: currentToken, user: currentUser });
      setToken(currentToken);
      setUser(currentUser);
      return currentUser;
    } catch (error) {
      if (isUnauthorizedError(error)) {
        clearAuthState();
      }
      throw error;
    }
  }, [auth, clearAuthState]);

  useEffect(() => {
    let active = true;

    async function restoreSession() {
      if (!storedAuth.token) {
        setLoading(false);
        return;
      }

      try {
        await refreshCurrentUser();
      } catch (error) {
        if (!isUnauthorizedError(error)) {
          console.warn("Failed to refresh current user.", error);
        }
      } finally {
        if (active) {
          setLoading(false);
        }
      }
    }

    restoreSession();

    return () => {
      active = false;
    };
  }, [refreshCurrentUser, storedAuth.token]);

  const login = useCallback(
    async (payload) => {
      const authResponse = await auth.login(payload);
      return persistAuthState(authResponse);
    },
    [auth, persistAuthState]
  );

  const register = useCallback(
    async (payload) => {
      const authResponse = await auth.register(payload);
      return persistAuthState(authResponse);
    },
    [auth, persistAuthState]
  );

  const logout = useCallback(async () => {
    try {
      await auth.logout();
    } catch (error) {
      console.warn("Logout request failed. Clearing local auth state.", error);
    } finally {
      clearAuthState();
    }
  }, [auth, clearAuthState]);

  const value = useMemo(
    () => ({
      user,
      token,
      loading,
      login,
      register,
      logout,
      refreshCurrentUser,
      isAuthenticated: Boolean(token && user)
    }),
    [loading, login, logout, refreshCurrentUser, register, token, user]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const value = useContext(AuthContext);

  if (!value) {
    throw new Error("AuthContext is not available.");
  }

  return value;
}
