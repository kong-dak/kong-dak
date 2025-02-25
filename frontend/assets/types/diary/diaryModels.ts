export interface DecorationType {
  type: string;
  content: string;
  positionX: Number;
  positionY: Number;
  style: string;
}
export interface DiaryType {
  content: string;
  emotion: string | null;
  weather: string | null;
  diaryDate: string;
  photoUrls: string[];
  decorations: DecorationType[] | null;
}
