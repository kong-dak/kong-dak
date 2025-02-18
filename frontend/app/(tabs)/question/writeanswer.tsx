import {
  createAnswers,
  getQuestion,
  modifyAnswers,
} from "@/assets/apis/daily-questions";
import { DailyQuestion } from "@/assets/types/question/questionModels";
import { AppText } from "@/components/common/AppText";
import HeaderIcons from "@/components/common/HeaderIcons";
import { Colors } from "@/constants/Colors";
import { FontAwesome6 } from "@expo/vector-icons";
import { router, useLocalSearchParams } from "expo-router";
import { useEffect, useState } from "react";
import {
  View,
  StyleSheet,
  ScrollView,
  TouchableOpacity,
  TextInput,
} from "react-native";
import { GestureHandlerRootView } from "react-native-gesture-handler";

export default function AnswerWriteScreen() {
  const { questionId, answerId, content } = useLocalSearchParams() as {
    questionId: string;
    answerId: string;
    content: string;
  };
  const [answerText, setAnswerText] = useState<string>(content);
  const [question, setQuestion] = useState<DailyQuestion>();
  const AnswerState =
    Number(answerId) === -1 && content === "" ? "POST" : "PATCH";

  useEffect(() => {
    if (!questionId) {
      router.back();
    }
    const detailQuestion = async () => {
      await getQuestion(Number(questionId)).then((res) => {
        console.log(res.data.data.answers);
        setQuestion(res.data.data);
      });
    };
    detailQuestion();
  }, []);

  const submitAnswer = async () => {
    console.log(questionId, answerText);
    if (AnswerState === "POST")
      await createAnswers(Number(questionId), answerText).then((res) => {
        console.log("POST 요청을 보냅니다. " + res);
        if (res.data.status === 201) {
          router.push({
            pathname: "/question/viewanswer",
            params: { questionId },
          });
        }
      });
    else {
      await modifyAnswers(
        Number(questionId),
        Number(answerId),
        answerText
      ).then((res) => {
        console.log("PATCH 요청을 보냅니다. " + res.data);
        router.push({
          pathname: "/question/viewanswer",
          params: { questionId },
        });
      });
    }
  };
  return (
    <View className="flex items-center" style={styles.container}>
      <HeaderIcons isBack={true} />
      <View className="w-full h-[25%] flex items-center">
        <View className="relative w-full flex items-center justify-center my-8">
          <AppText className="text-3xl text-center">오늘의 질문</AppText>
          <TouchableOpacity
            className="absolute right-1"
            onPress={() => {
              submitAnswer();
            }}
          >
            <FontAwesome6 name="pen-to-square" size={28} color={Colors.main} />
          </TouchableOpacity>
        </View>
        <View className="w-[80%] flex items-start justify-center my-8">
          <AppText className="text-2xl">{question?.title}</AppText>
        </View>
      </View>
      <AppText className="w-full text-xl mb-2 px-4">답변</AppText>

      <View className="w-[95%] max-h-[70%] bg-red-100 flex items-center ">
        <ScrollView>
          <View className="w-full flex items-center">
            <TextInput
              className="text-start"
              style={[
                styles.TextInput,
                { color: Colors.black, outline: "none" },
              ]}
              multiline={true}
              placeholder="답변 내용을 입력해주세요"
              placeholderTextColor={Colors.gray}
              value={answerText}
              onChangeText={(text) => {
                setAnswerText(text);
              }}
            />
          </View>
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
  TextInput: {
    padding: 4,
    marginBottom: 2, // 밑줄과의 간격
    outline: "none",
    outlineColor: "#929292",
    fontSize: 18,
    fontFamily: "GowunDodum-Regular",
  },
});
