import { AppText } from "@/components/common/AppText";
import HeaderIcons from "@/components/common/HeaderIcons";
import { Colors } from "@/constants/Colors";
import { router } from "expo-router";
import { View, StyleSheet, ScrollView, TouchableOpacity } from "react-native";

export default function QuestionScreen() {
  return (
    <View style={styles.container}>
      <HeaderIcons />
      <ScrollView className="text-3xl py-4 px-6">
        <TouchableOpacity onPress={() => router.push("/question/viewanswer")}>
          <AppText className="text-2xl my-8" style={{ color: Colors.main }}>
            3. 같이 하고싶은 것이 있나요?
          </AppText>
        </TouchableOpacity>
        <TouchableOpacity onPress={() => router.push("/question/viewanswer")}>
          <AppText className="text-2xl my-8" style={{ color: Colors.darkgray }}>
            2. 상대가 좋아하는 아이스크림은?
          </AppText>
        </TouchableOpacity>
        <TouchableOpacity onPress={() => router.push("/question/viewanswer")}>
          <AppText className="text-2xl my-8" style={{ color: Colors.darkgray }}>
            1. 상대를 좋아하게 된 이유가 무엇인가요?
          </AppText>
        </TouchableOpacity>
      </ScrollView>
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
