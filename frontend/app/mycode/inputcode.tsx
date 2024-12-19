import { AppText } from "@/components/common/AppText";
import { Colors } from "@/constants/Colors";
import { router } from "expo-router";
import { View, StyleSheet, TouchableOpacity, TextInput } from "react-native";

export default function InputCodeScreen() {
  return (
    <View className="section">
      <View className="h-[35%]"></View>
      <View className="h-[15%]">
        <View className="h-full flex items-center justify-between">
          <View className="flex items-center ">
            <AppText className="font-size-title">코드 입력</AppText>
            <AppText className="font-size-big my-2">
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
            />
            <View style={styles.underline} />
          </View>
        </View>
      </View>
      <View className="h-[40%]"></View>
      <View className="h-[10%]">
        <View className="flex flex-col items-center">
          <TouchableOpacity
            className="w-fit color-bg-main flex items-center justify-center py-2 px-6 rounded-md my-2"
            onPress={() => router.push("/(tabs)")}
          >
            <AppText color={Colors.white} className="">
              연결하기
            </AppText>
          </TouchableOpacity>
        </View>
      </View>
    </View>
  );
}
const styles = StyleSheet.create({
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
