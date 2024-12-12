import { AppText } from "@/components/common/AppText";
import { useNavigation } from "@react-navigation/native";
import { NativeStackNavigationProp } from "@react-navigation/native-stack";
import { router } from "expo-router";
import { View, Text, StyleSheet, TouchableOpacity } from "react-native";

export default function KakaoScreen() {
  return (
    <View className="color-bg-white h-full flex justify-between px-2 py-2">
      <View className="h-[30%]">
        <View className="h-[70%]" />
        <View className="flex">
          <AppText className="font-size-title">서로를 위한 다이어리</AppText>
          <AppText className="font-size-title">커플리❤</AppText>
        </View>
      </View>

      <View>
        <TouchableOpacity
          className=" bg-yellow-300 flex items-center justify-center py-2 rounded-sm my-2"
          onPress={() => router.push("/login/setnick")}
        >
          <Text>동의하고 계속하기</Text>
        </TouchableOpacity>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  kakao: {
    backgroundColor: "#b88c6f",
    paddingVertical: 8,
    paddingHorizontal: 24,
    borderRadius: 4,
    alignItems: "center",
    justifyContent: "center",
  },
  text: {
    fontFamily: "GowunDodum-Regular",
    fontSize: 16,
    color: "#FFFFFF",
  },
  disabled: {
    backgroundColor: "#BDBDBD",
  },
});
