import { TextStyle, ViewStyle } from "react-native";
import { DateData } from "react-native-calendars";

export interface CalendarType {
  checkDate: string;
  setCheckDate: React.Dispatch<React.SetStateAction<string>>;
}
export interface SchedulePeriod {
  scheduleId: number;
  startTime: string;
  endTime: string;
  color: string;
  title: string;
  idx: number;
}
export interface MarkedProps {
  startingDay: string;
  endingDay: string;
  color: string;
  title: string;
  idx: number;
}
export interface MarkedDateProps {
  periods: MarkedProps[];
}
export interface CustomDateData extends DateData {
  schedule?: MarkedDateProps; // 또는 더 구체적인 타입을 지정할 수 있습니다
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
    periods: SchedulePeriod[];
    color?: string;
    customStyles?: {
      container?: ViewStyle;
      text?: TextStyle;
    };
  };
  state?: "selected" | "disabled" | "today" | "";
  onDayPress?: (date: DateData) => void; // DateData 타입으로 변경
}
