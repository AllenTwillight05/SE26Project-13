import { useEffect, useState } from "react";

export function useAsyncData(loader, deps) {
  const [state, setState] = useState({
    data: null,
    loading: true,
    error: null
  });

  useEffect(() => {
    let active = true;

    // 保留旧数据，只刷新 loading，避免页面切换时先闪成空白。
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
      .catch((error) => {
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
      // 组件卸载后阻止过期请求回写状态，避免 React 警告。
      active = false;
    };
  }, deps);

  return state;
}
