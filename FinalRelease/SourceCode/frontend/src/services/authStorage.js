export const AUTH_STORAGE_KEY = "englishLearningCopilot.auth";

export function getStoredAuth() {
  if (typeof window === "undefined") {
    return { token: null, user: null };
  }

  try {
    const rawValue = window.localStorage.getItem(AUTH_STORAGE_KEY);

    if (!rawValue) {
      return { token: null, user: null };
    }

    const parsedValue = JSON.parse(rawValue);

    return {
      token: parsedValue?.token ?? null,
      user: parsedValue?.user ?? null
    };
  } catch {
    clearStoredAuth();
    return { token: null, user: null };
  }
}

export function setStoredAuth({ token, user }) {
  if (typeof window === "undefined") {
    return;
  }

  window.localStorage.setItem(AUTH_STORAGE_KEY, JSON.stringify({ token, user }));
}

export function clearStoredAuth() {
  if (typeof window === "undefined") {
    return;
  }

  window.localStorage.removeItem(AUTH_STORAGE_KEY);
}
