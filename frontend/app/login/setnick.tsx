import { AppButton } from "@/components/common/AppButton";
import { AppText } from "@/components/common/AppText";
import { Colors } from "@/constants/Colors";
import Checkbox from "expo-checkbox";
import { router } from "expo-router";
import { useState } from "react";
import { View, StyleSheet, TouchableOpacity } from "react-native";
import {
  GestureHandlerRootView,
  TextInput,
} from "react-native-gesture-handler";

export default function SetNicknameScreen() {
  const [isChecked, setIsChecked] = useState<boolean>(false);
  const [isChecked2, setIsChecked2] = useState<boolean>(false);
  const [nickText, setNickText] = useState<string>("");
  return (
    <View className="section" style={styles.container}>
      <View className="h-[35%]"></View>
      <View className="h-[15%]">
        <View className="h-full flex items-center justify-between">
          <View className="flex items-center ">
            <AppText className="text-2xl">별명 설정</AppText>
            <AppText className="text-sm text-gray-500">
              미입력시 별명이 랜덤으로 정해집니다.
            </AppText>
          </View>
          <GestureHandlerRootView className="flex items-center">
            <TextInput
              className="text-center"
              style={[styles.TextInput, { outline: "none" }]}
              placeholder="사용할 별명을 입력해주세요"
              placeholderTextColor={Colors.gray}
            />
            <View style={styles.underline} />
          </GestureHandlerRootView>
        </View>
      </View>
      <View className="h-[25%]"></View>
      <View className="h-[25%]">
        <View className="flex flex-row items-center my-1 ml-14">
          <Checkbox
            style={styles.checkbox}
            value={isChecked}
            onValueChange={setIsChecked}
            color={isChecked ? Colors.main : "#929292"}
          />
          <AppText>이용약관(필수)</AppText>
        </View>
        <View className="flex flex-row items-center my-1 ml-14 mb-8">
          <Checkbox
            style={styles.checkbox}
            value={isChecked2}
            onValueChange={setIsChecked2}
            color={isChecked2 ? Colors.main : "#929292"}
          />
          <AppText>개인정보 수집동의(필수)</AppText>
        </View>
        <View className="flex items-center justify-center">
          <AppButton
            text="시작하기"
            type="main"
            onPress={() => router.push("/(tabs)")}
          />
        </View>
      </View>
    </View>
  );

  function onChangeNickText(text: string) {
    setNickText(text);
  }
}

const styles = StyleSheet.create({
  container: {
    backgroundColor: "#fefefe",
    display: "flex",
    padding: 2,
    flex: 1,
  },
  checkbox: {
    marginRight: 8,
  },
  TextInput: {
    padding: 4,
    marginBottom: 2, // 밑줄과의 간격
    outline: "none",
    width: "110%", // 입력창 전체 너비 사용
    outlineColor: "#929292",
  },
  underline: {
    width: "120%", // 텍스트 길이의 2배
    height: 1,
    backgroundColor: Colors.gray,
    alignSelf: "center", // 중앙 정렬
  },
});
