import { AppText } from "@/components/common/AppText";
import HeaderIcons from "@/components/common/HeaderIcons";
import { Colors } from "@/constants/Colors";
import { router } from "expo-router";
import { View, StyleSheet, ScrollView, TouchableOpacity } from "react-native";

export default function AnswerViewScreen() {
  return (
    <View className="flex items-center" style={styles.container}>
      <HeaderIcons />
      <View className="h-[25%] flex items-center">
        <View className="w-full flex items-center justify-center my-8">
          <AppText className="text-3xl text-center">오늘의 질문</AppText>
        </View>
        <View className="w-[80%] flex items-start justify-center my-8">
          <AppText className="text-2xl">같이 하고싶은 것은 무엇인가요?</AppText>
          <AppText className="text-2xl my-2">상대방과 공유해보세요</AppText>
        </View>
      </View>
      <View className="w-[90%] h-[35%] flex items-start ">
        <AppText className="text-xl ">철수</AppText>
        <ScrollView>
          <AppText className="text-lg my-2" style={{ color: Colors.gray }}>
            질문에 대한 답변 후 확인 할 수 있습니다.
          </AppText>
        </ScrollView>
      </View>
      <View className="w-[90%] h-[35%] flex items-start ">
        <AppText className="text-xl ">영희</AppText>
        <TouchableOpacity
          onPress={() => {
            router.push("/question/writeanswer");
          }}
        >
          <AppText className="text-lg my-2" style={{ color: Colors.gray }}>
            답변을 작성해주세요.
          </AppText>
        </TouchableOpacity>
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
