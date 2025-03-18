import { StyleSheet, View, Pressable } from "react-native";

import { router } from "expo-router";
import { AppText } from "@/components/common/AppText";
import { Colors } from "@/constants/Colors";
import HeaderIcons from "@/components/common/HeaderIcons";
import { useEffect, useState } from "react";
import { memberInfo } from "@/assets/apis/members";
import { getDailyQuestion } from "@/assets/apis/daily-questions";
import { DailyQuestion } from "@/assets/types/question/questionModels";
import { monthlySchedules } from "@/assets/apis/calendars";
import { SchedulePeriod } from "@/assets/types/calendar/calendarModels";
import CalendarDetailList from "@/components/ui/CalendarDetailList";

export default function HomeScreen() {
  const [userNick, setUserNick] = useState<string>("멀쩡한 바지");
  const [isConnected, setIsConnected] = useState<boolean>(false);
  const [partnerNick, setPartnerNick] = useState<string>("멀쩡한 셔츠");
  const [dailyQuestion, setDailyQuestion] = useState<DailyQuestion>();
  const [calendarData, setCalendarData] = useState<SchedulePeriod[]>([]);
  const today = new Date();
  const year = today.getFullYear();
  const month = today.getMonth() + 1;
  const initialDate = year + "-" + month.toString().padStart(2, "0");
  const calendarColor: string[] = [
    "#F08484",
    "#F9A686",
    "#F9BF64",
    "#A0D6B6",
    "#30BA96",
    "#FFBB77",
  ];
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
    const getMonthlySchedules = async (dateTime: string) => {
      monthlySchedules(dateTime).then((res) => {
        const data = res.data.data.schedules;
        const newdata: SchedulePeriod[] = [];
        data.map(
          (
            item: {
              scheduleId: number;
              title: string;
              startTime: string;
              endTime: string;
            },
            index: number
          ) => {
            const calendarProcess: SchedulePeriod = {
              scheduleId: item.scheduleId,
              title: item.title,
              startTime: item.startTime,
              endTime: item.endTime,
              idx: 0,
              color: calendarColor[index % calendarColor.length],
            };
            newdata.push(calendarProcess);
          }
        );
        setCalendarData(newdata);
      });
    };
    getMonthlySchedules(initialDate);
    member();
    question();
    console.log(calendarData.length);
  }, []);

  useEffect(() => {
    console.log("캘린더 데이터입니다", calendarData);
  }, [calendarData]);

  return (
    <View
      className="relative flex flex-col justify-between h-full"
      style={styles.container}
    >
      <View className="relative w-full items-center">
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
            <AppText className="text-xl">
              Day {dailyQuestion?.questionId}
            </AppText>
            <AppText className="text-xl my-2">{dailyQuestion?.title}</AppText>
          </View>
        </Pressable>
      </View>
      <View className=" h-[50%] mx-4">
        <View className="my-2">
          <AppText className="text-xl">다가오는 일정</AppText>
        </View>
        <View className="w-full h-full">
          {calendarData.length != 0 ? (
            calendarData.map((item, index) => {
              return (
                <CalendarDetailList
                  key={"캘린더 디테일: " + index}
                  title={item.title}
                  color={item.color}
                  idx={item.idx}
                  startTime={item.startTime}
                  endTime={item.endTime}
                  myLatitude={0}
                  myLongitude={0}
                  scheduleId={item.scheduleId}
                />
              );
            })
          ) : (
            <Pressable
              onPress={() => {
                router.push("/calendar");
              }}
              className="relative w-full h-[20%] rounded-md my-2 p-2 border flex flex-row items-center justify-between"
            >
              <AppText
                className="text-lg w-[85%]"
                style={{ color: Colors.black }}
              >
                일정을 추가해주세요
              </AppText>
              <View className="w-[15%] flex items-center justify-center">
                <View className="w-12 h-12 border flex items-center justify-center border-dotted">
                  <AppText className="text-2xl" style={{ textAlign: "center" }}>
                    +
                  </AppText>
                </View>
              </View>
            </Pressable>
          )}
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
