import localAxios from "../utils/http-common";
import {
  CreateReviewRequest,
  SearchParams,
  UpdateReviewRequest,
} from "../types/map/mapModels";

const local = localAxios();

/**지도 검색 */
export async function search(params: SearchParams) {
  return await local.get(`/api/maps/search`, {
    params: {
      ...params,
    },
  });
}

/**장소 상세 정보 조회 */
export async function placeDetail(placeId: number) {
  return await local.get(`/api/maps/${placeId}`);
}

/** 장소 리뷰 조회 */
export async function getReviewsByPlace(placeId: number, page: number) {
  return await local.get(`/api/maps/${placeId}/reviews?page=${page}`);
}

/** 사용자 리뷰 조회 */
export async function getMyReviews(page: number) {
  return await local.get(`/api/maps/reviews/myReviews?page=${page}`);
}

/** 특정 리뷰 상세 조회 */
export async function getReviewById(reviewId: number) {
  return await local.get(`/api/maps/reviews/${reviewId}`);
}

/** 리뷰 작성 */
export async function createReview(
  placeId: number,
  request: CreateReviewRequest
) {
  return await local.post(`/api/maps/${placeId}/reviews`, request);
}

/** 리뷰 수정 */
export async function updateReview(
  reviewId: number,
  request: UpdateReviewRequest
) {
  return await local.patch(`/api/maps/reviews/${reviewId}`, request);
}

/** 리뷰 삭제 */
export async function deleteReview(reviewId: number) {
  return await local.delete(`/api/maps/reviews/${reviewId}`);
}
