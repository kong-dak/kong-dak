import { DetailListProps } from "@/assets/types/calendar/calendarUI";
import { AppText } from "../common/AppText";
import { Pressable, View } from "react-native";
import { MarkedProps } from "@/assets/types/calendar/calendarModels";
import { router } from "expo-router";

export default function CalendarDetailList({
  title,
  color,
  startingDay,
  endingDay,
  idx,
}: MarkedProps) {
  return (
    <Pressable
      onPress={() => {
        router.push({
          pathname: "/calenderregist",
          params: {
            title,
            color,
            startingDay,
            endingDay,
            idx,
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
