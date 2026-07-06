import { type DependencyList, useEffect, useState } from "react";

interface AsyncState<T> {
  data: T | null;
  loading: boolean;
  error: Error | null;
}

export function useAsyncData<T>(
  loader: () => Promise<T>,
  deps: DependencyList
): AsyncState<T> {
  const [state, setState] = useState<AsyncState<T>>({
    data: null,
    loading: true,
    error: null
  });

  useEffect(() => {
    let active = true;

    setState((current) => ({
      data: current.data,
      loading: true,
      error: null
    }));

    loader()
      .then((data) => {
        if (!active) {
          return;
        }

        setState({
          data,
          loading: false,
          error: null
        });
      })
      .catch((error: Error) => {
        if (!active) {
          return;
        }

        setState({
          data: null,
          loading: false,
          error
        });
      });

    return () => {
      active = false;
    };
  }, deps);

  return state;
}

