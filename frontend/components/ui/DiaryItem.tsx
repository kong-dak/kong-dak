import { ImageBackground, TouchableOpacity, View } from "react-native";
import { AppText } from "../common/AppText";
import { Feather } from "@expo/vector-icons";
import { Colors } from "@/constants/Colors";
import { router } from "expo-router";

export default function DiaryItem() {
  return (
    <TouchableOpacity
      className="relative w-full h-56 bg-white border p-3 rounded-xl border-gray-300"
      onPress={() => router.push("/write/edit")}
    >
      <ImageBackground
        source={require("../../assets/images/diary-list-sample.png")}
        className="h-[80%] p-2 rounded-xl"
        resizeMode="cover"
      >
        <View>
          <AppText className="text-3xl">11</AppText>
          <AppText className="text-xl">MON</AppText>
        </View>
      </ImageBackground>
      <View className="h-[20%] flex items-center justify-center">
        <AppText className="w-full">내가 만약 너를</AppText>
      </View>
      <Feather
        className="absolute right-0 top-0"
        name="paperclip"
        size={24}
        color={Colors.main}
      />
    </TouchableOpacity>
  );
}
