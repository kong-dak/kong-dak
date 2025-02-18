export interface DailyQuestion {
  answers: AnswerDetail[];
  questionId: number;
  title: string;
  bothAnswered: boolean;
}
export interface QuestionListItem {
  questionId: number;
  title: string;
}

export interface AnswerDetail {
  answerId: number;
  content: string;
  createdAt: string;
  isVisible: boolean;
  memberId: number;
}
