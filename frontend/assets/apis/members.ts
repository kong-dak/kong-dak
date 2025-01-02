import localAxios from '../utils/http-common';

const local = localAxios();

// /**로그인 */
// export async function login() {
//     return await local.get(`/api/members/login`);
// }

// /**로그아웃 */
// export async function logout() {
//     return await local.get(`/api/auth/logout`);
// }

/**회원 조회 */
export async function memberInfo() {
    return await local.get(`/api/members`);
}

/**닉네임 설정 */
export async function setNickname() {
    return await local.patch(`/api/members/nickname`);
}

/**회원 탈퇴 */
export async function deactivateMember() {
    return await local.delete(`/api/members`);
}