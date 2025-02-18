import { getHistory } from "@/assets/apis/daily-questions";
import { QuestionListItem } from "@/assets/types/question/questionModels";
import { AppText } from "@/components/common/AppText";
import HeaderIcons from "@/components/common/HeaderIcons";
import { Colors } from "@/constants/Colors";
import { router } from "expo-router";
import { useEffect, useState } from "react";
import { View, StyleSheet, ScrollView, TouchableOpacity } from "react-native";

export default function QuestionScreen() {
  const [questionList, setQuestionList] = useState<QuestionListItem[]>();

  useEffect(() => {
    const question = async () => {
      await getHistory().then((res) => {
        if (res.data.status === 200) {
          const data = res.data.data;
          setQuestionList(res.data.data);
          // console.log("질문 리스트");
          // data.map((item: QuestionListItem) => {
          //   console.log(item.questionId + " " + item.title);
          // });
        }
      });
    };
    question();
  }, []);
  return (
    <View style={styles.container}>
      <HeaderIcons />
      <ScrollView className="text-3xl py-4 px-6">
        {questionList?.map((item, index) => {
          return (
            <TouchableOpacity
              onPress={() =>
                router.push({
                  pathname: `/question/viewanswer`,
                  params: { questionId: item?.questionId },
                })
              }
            >
              <AppText
                className="text-2xl my-8"
                style={
                  index === 0
                    ? { color: Colors.main }
                    : { color: Colors.darkgray }
                }
              >
                {questionList.length - index + ". "}
                {item?.title}
              </AppText>
            </TouchableOpacity>
          );
        })}
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
