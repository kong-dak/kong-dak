import {
  GestureResponderEvent,
  ImageSourcePropType,
  TextStyle,
  ViewStyle,
} from "react-native";
import { DateData } from "react-native-calendars";

export type BucketType = "ALL" | "PLACE" | "EAT" | "TODO";

export interface CalenderType {
  checkDate: string;
  setCheckDate: React.Dispatch<React.SetStateAction<string>>;
}
export interface DayProps {
  date: {
    day: number;
    month: number;
    year: number;
    timestamp: number;
    dateString: string;
  };
  marking?: {
    marked?: boolean;
    selected?: boolean;
    customStyles?: {
      container?: ViewStyle;
      text?: TextStyle;
    };
  };
  state?: "selected" | "disabled" | "today" | "";
  onDayPress?: (date: DateData) => void; // DateData 타입으로 변경
}

export interface ScheduleStyleProps {
  marked: boolean;
  customStyles: {
    container: {
      flexDirection?: string;
      alignItems?: string;
      height?: number;
    };
    wrapper: {
      backgroundColor?: string;
      padding?: number;
      borderRadius?: number;
      marginTop?: number;
    };
    scheduleText: {
      fontSize?: number;
      color?: string;
    };
  };
}
export interface MarkedDatesType {
  [key: string]: ScheduleStyleProps;
}
export interface DiaryItemProps {
  content: string;
  diaryDate: string;
  diaryId: number;
  thumbnailUrl: string;
  weather: string;
}

export interface AppButtonProps {
  text: string; // 버튼 텍스트
  type: string;
  onPress: (event: GestureResponderEvent) => void; // 버튼 클릭 이벤트
  style?: ViewStyle; // 버튼의 스타일
  disabled?: boolean; // 버튼 비활성화 여부
  outline?: boolean;
}

export type BarType = "white" | "yellow";
export interface BarButtonProps {
  text: string; // 버튼 텍스트
  color?: BarType;
  imgSrc?: ImageSourcePropType;
  onPress: (event: GestureResponderEvent) => void; // 버튼 클릭 이벤트
  style?: ViewStyle; // 버튼의 스타일
}

export interface BucketListItem {
  bucketId: number;
  title: string;
  category: string;
  isCompleted: boolean;
  createdAt: string;
  updatedAt: string;
  order: number;
}
