// 统一的最小 fetch 封装。
// 后续如果要接 token、鉴权头、错误码映射，就集中改这里。
export async function getJson(url, init) {
  const response = await fetch(url, {
    ...init,
    headers: {
      Accept: "application/json",
      ...(init?.headers ?? {})
    }
  });

  if (!response.ok) {
    throw new Error(`Request failed: ${response.status} ${response.statusText}`);
  }

  return response.json();
}
