import { DetailListProps } from "@/assets/types/calendar/calendarUI";
import { AppText } from "../common/AppText";
import { Pressable, View } from "react-native";
import { MarkedProps } from "@/assets/types/calendar/calendarModels";
import { router } from "expo-router";

export default function CalendarDetailList({
  title,
  color,
  startTime,
  endTime,
  idx,
  scheduleId,
  myLatitude,
  myLongitude,
}: MarkedProps) {
  return (
    <Pressable
      onPress={() => {
        router.push({
          pathname: "/calenderregist",
          params: {
            scheduleId,
            title,
            color,
            startTime,
            endTime,
            idx,
            type: "EDIT",
            myLatitude,
            myLongitude,
          },
        });
      }}
      className="h-[20%] rounded-md my-2 p-2"
      style={{ backgroundColor: color }}
    >
      <AppText className="text-white">{title}</AppText>
    </Pressable>
  );
}
