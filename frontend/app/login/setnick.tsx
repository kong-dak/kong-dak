import { setNickname } from "@/assets/apis/members";
import { AppButton } from "@/components/common/AppButton";
import { AppText } from "@/components/common/AppText";
import { Colors } from "@/constants/Colors";
import Checkbox from "expo-checkbox";
import { router } from "expo-router";
import { ChangeEvent, useState } from "react";
import {
  View,
  StyleSheet,
  TouchableOpacity,
  Alert,
  ToastAndroid,
  KeyboardAvoidingView,
  TouchableWithoutFeedback,
  Platform,
  Keyboard,
  ScrollView,
} from "react-native";
import {
  GestureHandlerRootView,
  TextInput,
} from "react-native-gesture-handler";

export default function SetNicknameScreen() {
  const [isChecked, setIsChecked] = useState<boolean>(false);
  const [isChecked2, setIsChecked2] = useState<boolean>(false);
  const [nickText, setNickText] = useState<string>("");

  const changeNickName = async () => {
    if (!nickText || nickText.trim() === "") {
      Alert.alert("알림", "닉네임을 입력해주세요.", [{ text: "확인" }]);
      return; // 함수 실행 중단
    }
    if (!isChecked || !isChecked2) {
      ToastAndroid.show(
        "필수 이용약관을 동의해야 서비스 이용이 가능합니다.",
        4
      );
      return; // 함수 실행 중단
    }
    await setNickname(nickText).then((res) => {
      if (res.data.status === 200) {
        router.navigate("/(tabs)");
      }
    });
  };
  return (
    <KeyboardAvoidingView
      behavior={Platform.OS === "ios" ? "padding" : "height"}
      style={styles.container}
      keyboardVerticalOffset={Platform.OS === "ios" ? 64 : 0}
    >
      {/* 이외의 부분을 누르면 키보드 닫힘 */}
      <TouchableWithoutFeedback onPress={Keyboard.dismiss}>
        <ScrollView contentContainerStyle={styles.scrollContainer}>
          <View className="h-[35%]"></View>
          <View className="h-[15%]">
            <View className="w-full h-full flex items-center">
              <View className="h-[40%] flex items-center mb-10">
                <AppText className="text-3xl">별명 설정</AppText>
              </View>
              <View className="w-full h-[25%] flex items-center justify-center">
                <GestureHandlerRootView className="w-full flex items-center">
                  <TextInput
                    className="w-full text-center text-xl"
                    style={[styles.TextInput, { outline: "none" }]}
                    placeholder="사용할 별명을 입력해주세요"
                    placeholderTextColor={Colors.gray}
                    value={nickText}
                    onChangeText={(text) => {
                      setNickText(text);
                    }}
                  />
                </GestureHandlerRootView>
              </View>
              <View className="w-full " style={styles.underline} />
              <View className="mt-4">
                <AppText className="text-lg" style={{ color: Colors.black }}>
                  · 한글, 영문, 숫자 최소 2자에서 최대 8자
                </AppText>
                <AppText className="text-lg" style={{ color: Colors.black }}>
                  · 미입력시 별명이 랜덤으로 정해집니다.
                </AppText>
                <AppText className="text-lg" style={{ color: Colors.black }}>
                  · 닉네임은 추후 변경 가능합니다.
                </AppText>
              </View>
            </View>
          </View>
          <View className="h-[25%]"></View>
          <View className="h-[25%] flex items-center">
            <View className="flex flex-row items-center justify-start my-1 w-[60%]">
              <Checkbox
                style={styles.checkbox}
                value={isChecked}
                onValueChange={setIsChecked}
                color={isChecked ? Colors.main : "#929292"}
              />
              <AppText style={{ fontSize: 18 }}>이용약관(필수)</AppText>
            </View>
            <View className="flex flex-row items-center justify-start mt-2 mb-4 w-[60%]">
              <Checkbox
                style={styles.checkbox}
                value={isChecked2}
                onValueChange={setIsChecked2}
                color={isChecked2 ? Colors.main : "#929292"}
              />
              <AppText style={{ fontSize: 18 }}>
                개인정보 수집동의(필수)
              </AppText>
            </View>
            <View className="flex items-center justify-center w-[50%] mt-4">
              <AppButton
                text="시작하기"
                type="main"
                onPress={() => {
                  changeNickName();
                }}
                style={{ width: "100%" }}
                size="big"
              />
            </View>
          </View>
        </ScrollView>
      </TouchableWithoutFeedback>
    </KeyboardAvoidingView>
  );
}

const styles = StyleSheet.create({
  container: {
    backgroundColor: "#fefefe",
    flex: 1,
  },
  scrollContainer: {
    padding: 2,
    flexGrow: 1,
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
    width: "70%", // 텍스트 길이의 2배
    height: 1,
    alignSelf: "center", // 중앙 정렬
    borderBottomWidth: 1,
    borderBottomColor: Colors.gray,
  },
});
