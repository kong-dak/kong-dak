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
