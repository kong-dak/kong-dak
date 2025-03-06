import localAxios from "../utils/http-common";

const local = localAxios();

/**연결 코드 조회 */
export async function getCode() {
  return await local.get(`/api/couples/code`);
}

/**커플 연결 */
export async function connect() {
  return await local.post(`/api/couples`);
}

/**커플 매칭 요청 */
export async function matchReq(code: string) {
  return await local.post(`/api/couples/match`, { code });
}

/**커플 매칭 수락 */
export async function acceptReq(requestId: number) {
  return await local.post(`/api/couples/match/${requestId}/accept`, {
    requestId,
  });
}

/**커플 연결 해제 */
export async function disconnect(coupleId: number) {
  return await local.patch(`/api/couples/${coupleId}/disconnect`);
}

/**커플 연결 복구 */
export async function restore(coupleId: number) {
  return await local.patch(`/api/couples/${coupleId}/restore`);
}
