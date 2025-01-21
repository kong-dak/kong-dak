import localAxios from "../utils/http-common";
import { SearchParams } from "../types/map/mapModels";

const local = localAxios();

/**지도 검색 */
export async function search(params: SearchParams) {
  return await local.get(`/api/maps/search`, {
    params: {
      ...params,
    },
  });
}
