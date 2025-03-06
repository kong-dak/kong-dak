import { StyleSheet, View, Pressable } from "react-native";

import { router } from "expo-router";
import { AppText } from "@/components/common/AppText";
import { Colors } from "@/constants/Colors";
import HeaderIcons from "@/components/common/HeaderIcons";
import { useEffect, useState } from "react";
import { memberInfo } from "@/assets/apis/members";
import { getDailyQuestion } from "@/assets/apis/daily-questions";
import { DailyQuestion } from "@/assets/types/question/questionModels";

export default function HomeScreen() {
  const [userNick, setUserNick] = useState<string>("멀쩡한 바지");
  const [isConnected, setIsConnected] = useState<boolean>(false);
  const [partnerNick, setPartnerNick] = useState<string>("멀쩡한 셔츠");
  const [dailyQuestion, setDailyQuestion] = useState<DailyQuestion>();
  useEffect(() => {
    const member = async () => {
      await memberInfo().then((res) => {
        console.log(res.data);
        setUserNick(res.data.data.nickname);
        setPartnerNick(res.data.data.partnerNickname);
        setIsConnected(res.data.data.coupleInfo.isConnected);
      });
    };
    const question = async () => {
      await getDailyQuestion().then((res) => {
        if (res.data.status === 200) {
          console.log(res.data);
          setDailyQuestion(res.data.data);
        }
      });
    };
    member();
    question();
  }, []);

  return (
    <View className=" items-center" style={styles.container}>
      <HeaderIcons />
      <View className="w-full flex flex-row-reverse my-2">
        <AppText className="text-2xl mx-2">{userNick}</AppText>
      </View>
      <View className="w-full flex flex-row-reverse">
        {isConnected ? (
          <AppText
            color={Colors.gray}
            className="text-xl"
            style={{ color: Colors.black }}
          >
            {partnerNick}
          </AppText>
        ) : (
          <Pressable
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
          </Pressable>
        )}
      </View>

      <Pressable className="" onPress={() => router.push("/question")}>
        <View className="w-[95%] my-6 bg-gray-100 rounded py-3 px-6 flex flex-col items-center justify-center">
          <AppText className="text-xl">Day {dailyQuestion?.questionId}</AppText>
          <AppText className="text-xl my-2">{dailyQuestion?.title}</AppText>
        </View>
      </Pressable>
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
