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
