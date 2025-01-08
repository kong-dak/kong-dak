import { AppText } from "@/components/common/AppText";
import LongBarButton from "@/components/common/LongBarButton";
import { router } from "expo-router";
import { View, Text, StyleSheet, TouchableOpacity } from "react-native";
export default function KakaoLoginScreen() {
  const googleIcon = require("../../assets/images/react-logo.png");
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
          <LongBarButton
            text="카카오로 계속하기"
            color="yellow"
            onPress={() => router.push("/login/kakao")}
          />
          <LongBarButton
            text="Apple로 계속하기"
            color="white"
            onPress={() => router.push("/login/kakao")}
            style={{ marginVertical: 12 }}
          />
          <LongBarButton
            text="구글로 계속하기"
            color="white"
            imgSrc={googleIcon}
            onPress={() => router.push("/login/kakao")}
          />
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
