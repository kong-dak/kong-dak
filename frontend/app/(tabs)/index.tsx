import { StyleSheet, View, TouchableOpacity } from "react-native";

import { router } from "expo-router";
import { AppText } from "@/components/common/AppText";
import { AntDesign, Feather } from "@expo/vector-icons";
import { Colors } from "@/constants/Colors";
import HeaderIcons from "@/components/common/HeaderIcons";

export default function HomeScreen() {
  return (
    <View className=" items-center" style={styles.container}>
      <HeaderIcons />
      <View className="w-full flex flex-row-reverse my-2">
        <AppText className="text-2xl mx-2">멀쩡한 바지</AppText>
      </View>
      <View className="w-full flex flex-row-reverse">
        <TouchableOpacity
          className="flex items-center justify-center my-1 mx-2"
          onPress={() => router.push("/mycode")}
        >
          <AppText
            color={Colors.gray}
            className="text-xl"
            style={{ color: Colors.gray }}
          >
            상대를 연결해주세요
          </AppText>
        </TouchableOpacity>
      </View>

      <TouchableOpacity className="" onPress={() => router.push("/question")}>
        <View className="w-[95%] my-6 bg-gray-100 rounded py-3 px-6 flex flex-col items-center justify-center">
          <AppText className="text-xl">Day 1</AppText>
          <AppText className="text-xl my-2">
            상대와 하고싶은 데이트는 무엇인가요?
          </AppText>
        </View>
      </TouchableOpacity>
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
