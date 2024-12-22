import { AppText } from "@/components/common/AppText";
import { router } from "expo-router";
import { View, Text, StyleSheet, TouchableOpacity } from "react-native";

export default function KakaoLoginScreen() {
  return (
    <View
      className="section justify-between flex flex-col"
      style={styles.container}
    >
      <View className="h-[30%]">
        <View className="h-[70%]" />
        <View className="flex mx-10">
          <AppText className="text-2xl">서로를 위한 다이어리</AppText>
          <AppText className="text-2xl">커플리❤</AppText>
        </View>
      </View>

      <View className="w-full flex items-center">
        <View className="mb-4 w-[80%] flex flex-col justify-center">
          <TouchableOpacity
            className=" bg-yellow-300 flex items-center justify-center py-2 rounded-md my-2"
            onPress={() => router.push("/login/kakao")}
          >
            <Text>카카오로 계속하기</Text>
          </TouchableOpacity>
          <TouchableOpacity
            className="border-[1px] flex items-center justify-center py-2 rounded-md my-2"
            onPress={() => router.push("/login/kakao")}
          >
            <Text>Apple로 계속하기</Text>
          </TouchableOpacity>
          <TouchableOpacity
            className="border-[1px] flex items-center justify-center py-2 rounded-md my-2"
            onPress={() => router.push("/login/kakao")}
          >
            <Text>Google 계속하기</Text>
          </TouchableOpacity>
        </View>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    backgroundColor: "#fefefe",
    display: "flex",
    padding: 2,
    flex: 1,
  },
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
