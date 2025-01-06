import { TextStyle, ViewStyle } from "react-native";
import { DateData } from "react-native-calendars";

export type BucketType = "all" | "trip" | "food" | "do";

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
  datetime: string;
  diaryId: number;
  photos: string;
  weather: string;
}
