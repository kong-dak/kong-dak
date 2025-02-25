import localAxios from "../utils/http-common";

const local = localAxios();

/**월별 일정 조회 */
export async function refreshToken() {
  return await local.post(`/api/auth/refresh`);
}
export async function getLoginToken(provider: string, accessToken: string) {
  return await local.post(`/api/auth/login/${provider}`, {
    accessToken,
  });
}
