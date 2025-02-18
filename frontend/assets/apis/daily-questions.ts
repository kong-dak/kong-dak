import localAxios from "../utils/http-common";

const local = localAxios();

/**데일리 질문 조회 */
export async function getDailyQuestion() {
  return await local.get(`/api/daily-questions/current`);
}
/**질문 히스토리 조회 */
export async function getHistory() {
  return await local.get(`/api/daily-questions/history`);
}
/**질문 조회 */
export async function getQuestion(questionId: number) {
  return await local.get(`/api/daily-questions/${questionId}`);
}

/**데일리 질문 답변 조회 */
export async function getAnswers(questionId: number) {
  return await local.get(`/api/daily-questions/${questionId}/answers`);
}

/**데일리 질문 답변 생성 */
export async function createAnswers(questionId: number, content: string) {
  return await local.post(`/api/daily-questions/${questionId}/answers`, {
    content,
  });
}
/**데일리 질문 답변 수정 */
export async function modifyAnswers(
  questionId: number,
  answerId: number,
  content: string
) {
  return await local.patch(
    `/api/daily-questions/${questionId}/answers/${answerId}`,
    {
      content,
    }
  );
}

/**데일리 질문 답변 이모지 추가 */
export async function addEmoji(questionId: number, answerId: number) {
  return await local.patch(
    `/api/daily-questions/${questionId}/answers/${answerId}/emoji`
  );
}

/**데일리 질문 댓글 작성 */
export async function addReply(questionId: number) {
  return await local.post(`/api/daily-questions/${questionId}/replies`);
}

/**데일리 질문 댓글 삭제 */
export async function deleteReply(questionId: number, replyId: number) {
  return await local.post(
    `/api/daily-questions/${questionId}/replies/${replyId}`
  );
}
