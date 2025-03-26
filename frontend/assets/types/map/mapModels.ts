// 요청에 필요한 파라미터 타입 정의
export interface SearchParams {
  query: string; // 필수: 검색 키워드
  x?: string; // 선택: 중심 좌표 X (경도)
  y?: string; // 선택: 중심 좌표 Y (위도)
  radius?: string; // 선택: 검색 반경 (미터 단위)
  size?: string; // 선택: 결과 개수
  sort?: string; // 선택: 정렬 기준 (accuracy/distance)
}

// 응답 타입 정의
export interface SearchResponse {
  status: number; // HTTP 상태 코드
  success: boolean; // 성공 여부
  data: {
    meta: MetaData; // 메타데이터
    documents: Document[]; // 장소 정보 리스트
  };
  error: any | null; // 에러 정보 (null이면 에러 없음)
}

// Meta 타입 (응답 관련 정보)
export interface MetaData {
  total_count: number; // 검색된 문서 수
  pageable_count: number; // total_count 중 노출 가능 문서 수 (최대 45)
  is_end: boolean; // 현재 페이지가 마지막 페이지인지 여부
}

// Document 타입 (장소 정보)
export interface Document {
  id: string; // 장소 ID
  place_name: string; // 장소명, 업체명
  address_name: string; // 지번 주소
  category_name: string; // 카테고리 이름
  road_address_name: string; // 도로명 주소
  phone: string; // 전화번호
  x: string; // 경도
  y: string; // 위도
  place_url: string; // 장소 상세페이지 URL
  distance: string; // 중심 좌표까지의 거리 (미터 단위)
}

// 장소 상세 정보 응답 타입
export interface PlaceDetailResponse {
  placeId: number;
  placeName: string;
  categoryName: string;
  addressName: string;
  roadAddressName: string;
  phone?: string;
  previewImages: string[];
  previewReviews: PlaceReviewResponse[]; // 리뷰 정보
  operatingHours: PlaceOperatingHourResponse[]; // 운영 시간 정보
}

/** 장소 리뷰 응답 타입 */
export interface PlaceReviewResponse {
  nickname: string; // 리뷰 작성자 닉네임
  rating: number; // 별점
  comment: string; // 리뷰 내용
  images: PlaceImageResponse[]; // 리뷰 이미지 리스트
  createdAt: string; // 작성 시간 (ISO 8601 형식)
  updatedAt: string; // 수정 시간 (ISO 8601 형식)
}

/** 페이징 된 리뷰 응답 타입 */
export interface PagedReviewResponse {
  content: PlaceReviewResponse[];
  currentPage: number;
  totalPages: number;
  hasNext: boolean;
}

/** 장소 리뷰 삭제 응답 타입 */
export interface PlaceReviewDeleteResponse {
  reviewId: number;
  placeId: number;
  memberId: number;
  deleteAt: string; // 삭제된 시간 (ISO 8601 형식)
}

/** 장소 리뷰 이미지 응답 타입 */
export interface PlaceImageResponse {
  id: number; // 이미지 ID
  imageUrl: string; // 이미지 URL
  description?: string; // 이미지 설명 (선택 사항)
}

/** 장소 운영 시간 응답 타입 */
export interface PlaceOperatingHourResponse {
  dayOfWeek: string; // 요일
  openTime: string; // 오픈 시간
  closeTime: string; // 마감 시간
}

/** 리뷰 생성 요청 타입 */
export interface CreateReviewRequest {
  rating: number; // 별점 (필수)
  comment: string; // 리뷰 내용 (필수)
  imageUrls?: string[]; // 첨부된 이미지 URL 목록 (선택)
}

/** 리뷰 수정 요청 타입 */
export interface UpdateReviewRequest {
  rating: number; // 별점 (필수)
  comment: string; // 리뷰 내용 (필수)
  newImageUrls?: string[]; // 추가할 이미지 URL 목록 (선택)
  deleteImageIds?: number[]; // 삭제할 이미지 ID 목록 (선택)
}
