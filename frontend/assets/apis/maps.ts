import localAxios from '../utils/http-common';

const local = localAxios();

/**지도 검색 */
export async function search(query: string, display: number, start: number) {
    return await local.get(`/api/maps/search`, {
        params: {
            query: query,
            display: display,
            start: start,
        },
    });
}