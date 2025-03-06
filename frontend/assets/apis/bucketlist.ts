import { BucketType } from "../types/type";
import localAxios from "../utils/http-common";

const local = localAxios();

/** 버킷리스트 등록 */
export async function registBucket(title: string, category: BucketType) {
  return await local.post(`/api/bucketlists`, { title, category });
}

/** 버킷리스트 삭제 */
export async function deleteBucket(bucketlistId: number) {
  return await local.delete(`/api/bucketlists/${bucketlistId}`);
}

/** 버킷리스트 수정 */
export async function editBucket(bucketlistId: number) {
  return await local.patch(`api/bucketlists/${bucketlistId}`);
}

/** 버킷리스트 정렬 */
export async function reorderBucket(bucketIds: number[]) {
  return await local.patch(`api/bucketlists/reorder`, { bucketIds });
}

/** 버킷리스트 가져오기 */
export async function getBucket() {
  return await local.get(`api/bucketlists`);
}
