import { AppText } from "@/components/common/AppText";
import HeaderIcons from "@/components/common/HeaderIcons";
import { Colors } from "@/constants/Colors";
import { FontAwesome6 } from "@expo/vector-icons";
import { router } from "expo-router";
import { View, StyleSheet, ScrollView, TouchableOpacity } from "react-native";

export default function AnswerWriteScreen() {
  return (
    <View className="flex items-center" style={styles.container}>
      <HeaderIcons isBack={true} />
      <View className="w-full h-[25%] flex items-center">
        <View className="relative w-full flex items-center justify-center my-8">
          <AppText className="text-3xl text-center">오늘의 질문</AppText>
          <TouchableOpacity
            className="absolute right-1"
            onPress={() => router.push("/question/viewanswer")}
          >
            <FontAwesome6 name="pen-to-square" size={28} color={Colors.main} />
          </TouchableOpacity>
        </View>
        <View className="w-[80%] flex items-start justify-center my-8">
          <AppText className="text-2xl">같이 하고싶은 것은 무엇인가요?</AppText>
          <AppText className="text-2xl my-2">상대방과 공유해보세요</AppText>
        </View>
      </View>
      <View className="w-[90%] h-[35%] flex items-start ">
        <AppText className="text-xl ">답변</AppText>
        <ScrollView>
          <AppText className="text-xl my-2" style={{ color: Colors.black }}>
            아, 롤 하고싶다... 하지만 프로젝트하고 개발 하다보니까 게임 하고싶은
            생각이 점점 사라지고 새로운 것을 얻어가는게 마음에 들기 시작했어..
          </AppText>
        </ScrollView>
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
