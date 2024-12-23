import { AppButton } from "@/components/common/AppButton";
import { AppText } from "@/components/common/AppText";
import { Colors } from "@/constants/Colors";
import { router } from "expo-router";
import { View, Text, Button, TouchableOpacity, StyleSheet } from "react-native";

export default function CodeScreen() {
  return (
    <View className="" style={styles.container}>
      <View className="h-[35%]"></View>
      <View className="h-[15%]">
        <View className="h-full flex items-center justify-between">
          <View className="flex items-center ">
            <AppText className="text-xl">나의 공유코드</AppText>
            <AppText className="text-xl my-4" style={{ color: Colors.main }}>
              123465
            </AppText>
          </View>
        </View>
      </View>
      <View className="h-[25%]"></View>
      <View className="h-[20%]">
        <View className="flex flex-col items-center">
          <AppButton
            text="코드 공유"
            type="main"
            onPress={() => {}}
            style={{ marginBottom: 8 }}
          />
          <AppButton
            text="상대 코드 입력"
            type="main"
            onPress={() => router.push("/mycode/inputcode")}
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
});
