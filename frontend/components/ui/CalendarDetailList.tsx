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
      className="relative w-full h-[20%] rounded-md my-2 p-2"
      style={{ backgroundColor: color }}
    >
      <AppText className="text-white">{title}</AppText>
      <View className="absolute right-0 bottom-0">
        <AppText
          className="text-white mb-1 me-1"
          style={{ textAlign: "right" }}
        >
          {startTime.split("T")[0]}~{endTime.split("T")[0]}
        </AppText>
      </View>
    </Pressable>
  );
}
