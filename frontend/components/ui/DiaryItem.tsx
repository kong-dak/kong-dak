import { ImageBackground, Pressable, View } from "react-native";
import { AppText } from "../common/AppText";
import { Feather } from "@expo/vector-icons";
import { Colors } from "@/constants/Colors";
import { router } from "expo-router";
import { DiaryItemProps } from "@/assets/types/type";
import { getWeekday } from "@/assets/utils/getWeekday";

export default function DiaryItem(props: DiaryItemProps) {
  const content = props.content;
  const date = props.diaryDate.split("-");
  const month = date[1];
  const day = date[2];
  const dayText = getWeekday(props.diaryDate, "en");

  const getStyle = () => {
    if (dayText == "SAT") {
      return Colors.blue2;
    } else if (dayText === "SUN") return Colors.red1;
    else return Colors.black;
  };

  const dayTextColor = getStyle();

  return (
    <Pressable
      className="relative flex flex-row w-full h-32 bg-white border-2 p-3 rounded-xl"
      onPress={() =>
        router.push({
          pathname: `/write/edit`,
          params: {
            diaryId: props.diaryId,
            diaryDate: props.diaryDate,
            content: props.content,
            weather: props.weather,
            thumbnailUrl: props.thumbnailUrl,
          },
        })
      }
      style={{ borderColor: Colors.sublight }}
    >
      <View className="w-[15%] flex items-center justify-center">
        <Feather className="mb-2" name="sun" size={20} />
        <AppText className="text-3xl text-center font-bold">{day}</AppText>
        <AppText
          className="text-lg text-center"
          style={{ color: dayTextColor }}
        >
          {dayText}
        </AppText>
      </View>
      <View className="w-[85%] flex flex-row justify-between items-center">
        <View className="w-[75%] h-[80%] flex items-center justify-center">
          <AppText
            numberOfLines={2}
            ellipsizeMode="tail"
            className="w-full pe-2"
          >
            {content}
          </AppText>
        </View>
        <ImageBackground
          source={require("../../assets/images/diary-list-sample.png")}
          className="w-[25%]  p-2 rounded-xl"
          resizeMode="cover"
          style={{ aspectRatio: 1 / 1 }}
        ></ImageBackground>
      </View>
    </Pressable>
  );
}
