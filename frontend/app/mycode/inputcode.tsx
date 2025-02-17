import { acceptReq, matchReq } from "@/assets/apis/couples";
import { AppButton } from "@/components/common/AppButton";
import { AppText } from "@/components/common/AppText";
import { Colors } from "@/constants/Colors";
import { router } from "expo-router";
import { useState } from "react";
import {
  View,
  StyleSheet,
  TouchableOpacity,
  TextInput,
  Alert,
} from "react-native";

export default function InputCodeScreen() {
  const [code, setCode] = useState<string>("");

  const requestCode = async () => {
    if (!code || code.trim() === "") {
      Alert.alert("알림", "코드를 입력해주세요.", [{ text: "확인" }]);
      return; // 함수 실행 중단
    }

    await matchReq(code)
      .then((res) => {
        if (res.data.status === 200) {
          console.log(res.data);
          router.push("/(tabs)");
        } else {
          console.error(res.data);
        }
      })
      .catch((err) => {
        console.log(err);
      });
  };
  return (
    <View className="" style={styles.container}>
      <View className="h-[35%]"></View>
      <View className="h-[15%]">
        <View className="h-full flex items-center justify-between">
          <View className="flex items-center ">
            <AppText className="text-2xl">코드 입력</AppText>
            <AppText className="text-xl my-2">
              상대방의 코드를 입력해주세요.
            </AppText>
          </View>
          <View className="flex items-center mt-3">
            <TextInput
              className="text-center"
              style={[
                styles.TextInput,
                { color: Colors.main, outline: "none" },
              ]}
              placeholder="코드"
              placeholderTextColor={Colors.gray}
              value={code}
              onChangeText={(text) => {
                setCode(text);
              }}
            />
            <View style={styles.underline} />
          </View>
        </View>
      </View>
      <View className="h-[40%]"></View>
      <View className="h-[10%]">
        <View className="flex flex-col items-center">
          <AppButton
            text="연결하기"
            type="main"
            onPress={() => requestCode()}
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
  TextInput: {
    padding: 4,
    marginBottom: 2, // 밑줄과의 간격
    outline: "none",
    width: "110%", // 입력창 전체 너비 사용
    outlineColor: "#929292",
    fontSize: 24,
    fontFamily: "GowunDodum-Regular",
  },
  underline: {
    width: "60%", // 텍스트 길이의 2배
    height: 1,
    backgroundColor: Colors.gray,
    alignSelf: "center", // 중앙 정렬
  },
});
