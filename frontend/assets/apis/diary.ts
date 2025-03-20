import { DiaryType } from "../types/diary/diaryModels";
import localAxios from "../utils/http-common";

const local = localAxios();

/**월별 일기 조회 */
export async function getDiaryList(datetime: string) {
  return await local.get(`/api/diaries?datetime=${datetime}`);
}

/**일기 상세 조회 */
export async function getDiaryDetail(diaryId: Number) {
  return await local.get(`/api/diaries/${diaryId}`);
}

/** 일기 쓰기 */
export async function postDiary(params: DiaryType) {
  return await local.post(`/api/diaries`, params);
}

/** 일기 수정 */
export async function editDiary(diaryId: Number, params: DiaryType) {
  return await local.put(`/api/diaries/${diaryId}`, params);
}

/** 일기 삭제 */
export async function deleteDiary(diaryId: Number) {
  return await local.delete(`/api/diaries/${diaryId}`);
}

/** 이미지 등록 */
export async function uploadPhotos(formData: FormData) {
  // axios가 multipart/form-data를 자동으로 감지하도록 Content-Type 헤더를 명시적으로 설정하지 마세요
  return local.post("/api/diaries/photos", formData, {
    headers: {
      "Content-Type": "multipart/form-data",
      // 필요한 경우 다른 헤더 추가
    },
  });
}
