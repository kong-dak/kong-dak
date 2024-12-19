import { AppText } from "@/components/common/AppText";
import { Colors } from "@/constants/Colors";
import { router } from "expo-router";
import { View, Text, Button, TouchableOpacity } from "react-native";

export default function CodeScreen() {
  return (
    <View className="section">
      <View className="h-[35%]"></View>
      <View className="h-[15%]">
        <View className="h-full flex items-center justify-between">
          <View className="flex items-center ">
            <AppText className="font-size-big">나의 공유코드</AppText>
            <AppText color={Colors.main} className="font-size-big my-4">
              123465
            </AppText>
          </View>
        </View>
      </View>
      <View className="h-[25%]"></View>
      <View className="h-[20%]">
        <View className="flex flex-col items-center">
          <TouchableOpacity className="w-fit color-bg-main flex items-center justify-center py-2 px-6 rounded-md my-2">
            <AppText color={Colors.white} className="">
              코드 공유
            </AppText>
          </TouchableOpacity>
          <TouchableOpacity
            className="w-fit color-bg-main flex items-center justify-center py-2 px-6 rounded-md my-2"
            onPress={() => router.push("/mycode/inputcode")}
          >
            <AppText color={Colors.white} className="">
              상대 코드 입력
            </AppText>
          </TouchableOpacity>
        </View>
      </View>
    </View>
  );
}
