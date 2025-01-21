import {
  CalendarType,
  DayProps,
  MarkedProps,
  SchedulePeriod,
} from "@/assets/types/calendar/calendarModels";
import { generateMarkedDates, getSundayDates } from "@/assets/utils/calendar";
import { AppText } from "@/components/common/AppText";
import { Colors } from "@/constants/Colors";
import { AntDesign, Feather } from "@expo/vector-icons";
import { router } from "expo-router";
import { JSX, SetStateAction, useEffect, useState } from "react";
import {
  View,
  Text,
  Button,
  StyleSheet,
  TouchableOpacity,
  ViewStyle,
  TextStyle,
  Pressable,
  Modal,
} from "react-native";
import { Calendar, DateData, LocaleConfig } from "react-native-calendars";
import dummydata from "../../../assets/dummydata/calandarmonthlist.json";

export default function CalendarScreen() {
  const [currentDate, setCurrentDate] = useState(
    new Date().toISOString().split("T")[0]
  );
  const [checkDate, setCheckDate] = useState<string>("");
  const [calendarData, setCalendarData] = useState<SchedulePeriod[]>([]);
  const [isModalVisible, setIsModalVisible] = useState<boolean>(false);
  const calendarColor: string[] = [
    "#F08484",
    "#F9A686",
    "#F9BF64",
    "#A0D6B6",
    "#30BA96",
    "#FFBB77",
  ];

  useEffect(() => {
    const data = dummydata.data;
    const newdata: SchedulePeriod[] = [];
    data.map((item, index) => {
      const calendarProcess: SchedulePeriod = {
        scheduleId: item.scheduleId,
        title: item.title,
        startTime: item.startTime,
        endTime: item.endTime,
        idx: 0,
        color: calendarColor[index % calendarColor.length],
      };
      newdata.push(calendarProcess);
    });
    setCalendarData(newdata);
  }, []);
  LocaleConfig.locales["ko"] = {
    monthNames: [
      "01월",
      "02월",
      "03월",
      "04월",
      "05월",
      "06월",
      "07월",
      "08월",
      "09월",
      "10월",
      "11월",
      "12월",
    ],
    monthNamesShort: [
      "1월",
      "2월",
      "3월",
      "4월",
      "5월",
      "6월",
      "7월",
      "8월",
      "9월",
      "10월",
      "11월",
      "12월",
    ],
    dayNames: [
      "일요일",
      "월요일",
      "화요일",
      "수요일",
      "목요일",
      "금요일",
      "토요일",
    ],
    dayNamesShort: ["일", "월", "화", "수", "목", "금", "토"],
    today: "오늘",
  };
  LocaleConfig.defaultLocale = "ko";

  const CalendarView = ({ checkDate, setCheckDate }: CalendarType) => {
    // 기간 데이터를 markedDates 형식으로 변환
    const [year, month] = currentDate.split("-");
    const sundays = getSundayDates(year, month);
    const markedDates = generateMarkedDates(calendarData, year, month);

    // console.info(markedDates["2025-01-13"]);
    // console.info(markedDates["2025-01-14"]);
    for (let i = 0; i < sundays.length; i++) {
      if (markedDates[sundays[i]]) {
        markedDates[sundays[i]].periods.map((item: MarkedProps) => {});
      }
    }
    // 선택한 날짜에 대한 스타일 설정이 있다
    const markedSelectedDates = {
      ...markedDates,
      [checkDate]: {
        ...markedDates[checkDate], // 기존 일정 정보 유지
        selected: true,
      },
    };
    // 날짜 커스텀을 위한 컴포넌트
    const CustomDay: React.FC<DayProps> = ({
      date,
      state,
      marking,
      onDayPress,
    }) => {
      return (
        <Pressable
          onPress={() =>
            onDayPress?.({
              dateString: date.dateString,
              day: date.day,
              month: date.month,
              year: date.year,
              timestamp: date.timestamp,
            })
          }
          style={[
            {
              height: 130,
              alignItems: "center",
              width: "100%",
              position: "relative",
            },
          ]}
        >
          {/* 선택 요소에 대한 속성 */}
          {marking?.selected && (
            <View
              style={{
                position: "absolute",
                top: 0,
                left: 0,
                right: 0,
                bottom: 0,
                borderRadius: 5,
                borderWidth: 1,
                borderColor: Colors.sublight,
                zIndex: 1,
                pointerEvents: "none", // 터치 이벤트가 통과하도록
              }}
            />
          )}
          <Text
            style={{
              color: state === "disabled" ? "gray" : "black",
            }}
          >
            {date.day}
          </Text>

          {marking?.periods?.map((period, idx) => {
            const height = 22;
            const margin = period.idx - idx;
            return (
              <View
                style={{
                  height: height,
                  display: "flex",
                  justifyContent: "center",
                  alignItems: "center",
                  backgroundColor: period.color,
                  padding: 4,
                  marginTop: 4 + (margin > 0 ? margin * (height + 4) : 0),
                  width: "100%",
                }}
              >
                <Text
                  style={{
                    fontSize: 12,
                    color: "white",
                    textAlign: "center",
                  }}
                >
                  {period.title}
                </Text>
              </View>
            );
          })}
        </Pressable>
      );
    };
    return (
      //기본 캘린더 설정
      <Calendar
        theme={{
          "stylesheet.calendar.header": {
            dayTextAtIndex0: {
              color: Colors.red1, // 일요일 색상
            },
            dayTextAtIndex6: {
              color: Colors.blue3, // 토요일 색상
            },
          }, // 날짜 셀 크기 조절
          "stylesheet.day.basic": {
            base: {
              width: 30,
              height: 60,
              position: "flex",
              justifyContent: "start",
            },
            text: {
              fontSize: 16,
              color: "#2d4150",
              textAlign: "center",
            },
          }, // 달력 행 간격 조절
          "stylesheet.calendar.main": {
            week: {
              marginTop: 6,
              marginBottom: 6,
              flexDirection: "row",
              justifyContent: "space-around",
            },
          },

          // 날짜 텍스트 스타일
          dayTextColor: Colors.black,
          textDayFontSize: 16, //글씨크기

          // 달력 헤더, 날짜 관련
          // calendarBackground: "#ffffff",
          textSectionTitleColor: Colors.black,
          textMonthFontSize: 18,

          // 선택된 날짜 스타일
          selectedDayBackgroundColor: Colors.main,
          // selectedDayTextColor: "#ffffff",

          // 오늘 날짜 스타일
          todayTextColor: Colors.subbold,
        }}
        dayComponent={(props: DayProps) => (
          <CustomDay
            {...props}
            onDayPress={(day: DateData) => {
              setCheckDate(day.dateString);
              setIsModalVisible(true);
            }}
          />
        )}
        current={currentDate}
        onMonthChange={(date: DateData) => {
          setCurrentDate(date.dateString);
        }}
        markedDates={markedSelectedDates}
        markingType="period"
        //달 출력 포맷
        monthFormat={"yyyy / M"}
        renderArrow={(direction: string) =>
          direction === "left" ? (
            <Feather name="arrow-left" size={24} color={Colors.black} />
          ) : (
            <Feather name="arrow-right" size={24} color={Colors.black} />
          )
        }
      />
    );
  };

  return (
    <View className="items-center relative" style={styles.container}>
      <Text>Calendar화면입니다</Text>
      <View className="w-full h-[100%] bg-slate-200">
        <CalendarView checkDate={checkDate} setCheckDate={setCheckDate} />
      </View>
      <View style={{ marginTop: 400 }}>
        <Modal animationType="fade" visible={isModalVisible} transparent={true}>
          <View
            className="relative h-full w-full flex justify-center items-center"
            style={{ backgroundColor: "rgba(0,0,0,0.2)" }}
          >
            <View className="w-[80%] h-[60%] bg-white rounded-lg p-4">
              <View className="flex flex-row justify-between">
                <AppText className="text-xl">21일 목요일</AppText>
                <Pressable
                  className="right-6"
                  onPress={() => {
                    setIsModalVisible(false);
                  }}
                >
                  <AntDesign name="close" size={24} color={Colors.black} />
                </Pressable>
              </View>
              <View className="border-b py-2"></View>
              <View className="py-4">
                <AppText>음력 11월 21일</AppText>
              </View>
              {/* 여기 일정 컴포넌트 */}
              <Pressable
                className="absolute bottom-4 right-4 border p-2 rounded-full "
                style={{
                  borderColor: Colors.main,
                  backgroundColor: Colors.main,
                }}
                onPress={() => {
                  setIsModalVisible(false);
                  router.push("/calenderregist");
                }}
              >
                <AntDesign name="plus" size={24} color={Colors.white} />
              </Pressable>
            </View>
          </View>
        </Modal>
      </View>
      <Button
        title="Go to Map Screen"
        onPress={() => router.push("/calendar/mapscreen")}
      />
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
    padding: 2,
    outlineColor: Colors.main,
    outline: "none",
    fontSize: 16,
    fontFamily: "GowunDodum-Regular",
  },
});
