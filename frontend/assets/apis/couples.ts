import localAxios from '../utils/http-common';

const local = localAxios();

/**커플 연결 */
export async function connect() {
    return await local.post(`/api/couples`);
}

/**커플 연결 해제 */
export async function disconnect(coupleId: number) {
    return await local.patch(`/api/couples/${coupleId}/disconnect`);
}

/**커플 연결 해제 */
export async function restore(coupleId: number) {
    return await local.patch(`/api/couples/${coupleId}/restore`);
}