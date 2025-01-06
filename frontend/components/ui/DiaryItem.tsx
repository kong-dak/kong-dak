import { ImageBackground, Pressable, View } from "react-native";
import { AppText } from "../common/AppText";
import { Feather } from "@expo/vector-icons";
import { Colors } from "@/constants/Colors";
import { router } from "expo-router";
import { DiaryItemProps } from "@/assets/types/type";
import { getWeekday } from "@/assets/utils/getWeekday";

export default function DiaryItem(props: DiaryItemProps) {
  const content = props.content;
  const date = props.datetime.split("-");
  const month = date[1];
  const day = date[2];
  const dayText = getWeekday(props.datetime, "en");
  return (
    <Pressable
      className="relative w-full h-56 bg-white border p-3 rounded-xl border-gray-300"
      onPress={() => router.push("/write/edit")}
    >
      <ImageBackground
        source={require("../../assets/images/diary-list-sample.png")}
        className="h-[80%] p-2 rounded-xl"
        resizeMode="cover"
      >
        <View>
          <AppText className="text-3xl">{day}</AppText>
          <AppText className="text-xl">{dayText}</AppText>
        </View>
      </ImageBackground>
      <View className="h-[20%] flex items-center justify-center">
        <AppText numberOfLines={1} ellipsizeMode="tail" className="w-full">
          {content}
        </AppText>
      </View>
      <Feather
        className="absolute right-0 top-0"
        name="paperclip"
        size={24}
        color={Colors.main}
      />
    </Pressable>
  );
}
