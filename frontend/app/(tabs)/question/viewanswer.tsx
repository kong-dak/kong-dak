import { getQuestion } from "@/assets/apis/daily-questions";
import { memberInfo } from "@/assets/apis/members";
import {
  AnswerDetail,
  DailyQuestion,
} from "@/assets/types/question/questionModels";
import { AppText } from "@/components/common/AppText";
import HeaderIcons from "@/components/common/HeaderIcons";
import { Colors } from "@/constants/Colors";
import { router, useLocalSearchParams } from "expo-router";
import { useEffect, useState } from "react";
import { View, StyleSheet, ScrollView, TouchableOpacity } from "react-native";

export default function AnswerViewScreen() {
  const { questionId } = useLocalSearchParams();
  const [question, setQuestion] = useState<DailyQuestion>();
  const [myId, setMyId] = useState<number>();
  const [myAnswer, setMyAnswer] = useState<AnswerDetail>();
  const [coupleAnswer, setCoupleAnswer] = useState<AnswerDetail>();

  useEffect(() => {
    const fetchData = async () => {
      try {
        // 먼저 회원 정보 가져오기
        const memberResponse = await memberInfo();
        if (memberResponse.status === 200) {
          setMyId(memberResponse.data.data.memberId);

          // 회원 정보 저장 후 질문 상세정보 가져오기
          const questionResponse = await getQuestion(Number(questionId));
          console.log("질문 상세정보");
          console.log(
            questionResponse.data.data.answers,
            questionResponse.data.data.questionId
          );
          setQuestion(questionResponse.data.data);
        }
      } catch (error) {
        console.error("데이터 가져오기 실패:", error);
      }
    };

    fetchData();
  }, []);

  useEffect(() => {
    if (question?.answers) {
      question.answers.map((item) => {
        if (myId === item.memberId) {
          setMyAnswer(item);
        } else {
          setCoupleAnswer(item);
        }
      });
    }
  }, [question]);

  const coupleAnswerFilter = () => {
    if (myAnswer && coupleAnswer) {
      return coupleAnswer.content;
    } else if (!myAnswer && coupleAnswer) {
      return "질문에 대한 답변 후 확인 할 수 있습니다.";
    } else if (!coupleAnswer) {
      return "상대방이 답변을 작성하지 않았습니다.";
    }
    return "상대방이 답변을 작성하지 않았습니다.";
  };

  return (
    <View className="flex items-center" style={styles.container}>
      <HeaderIcons />
      <View className="h-[25%] flex items-center">
        <View className="w-full flex items-center justify-center my-8">
          <AppText className="text-3xl text-center">오늘의 질문</AppText>
        </View>
        <View className="w-[80%] flex items-start justify-center my-8">
          <AppText className="text-2xl">{question?.title}</AppText>
        </View>
      </View>
      <View className="w-[90%] h-[35%] flex items-start ">
        <AppText className="text-xl ">상대방</AppText>
        <ScrollView>
          <AppText className="text-lg my-2" style={{ color: Colors.gray }}>
            {coupleAnswerFilter()}
          </AppText>
        </ScrollView>
      </View>
      <View className="w-[90%] h-[35%] flex items-start ">
        <AppText className="text-xl ">나</AppText>
        <TouchableOpacity
          onPress={() => {
            router.push({
              pathname: "/question/writeanswer",
              params: {
                questionId,
                answerId: myAnswer ? myAnswer.answerId : -1,
                content: myAnswer ? myAnswer.content : "",
              },
            });
          }}
        >
          <AppText
            className="text-lg my-2"
            style={{ color: myAnswer ? Colors.black : Colors.gray }}
          >
            {myAnswer ? myAnswer.content : "답변을 작성해주세요."}
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
