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

interface CalenderType {
  checkDate: string;
  setCheckDate: React.Dispatch<React.SetStateAction<string>>;
}
interface DayProps {
  date: {
    day: number;
    month: number;
    year: number;
    timestamp: number;
    dateString: string;
  };
  marking?: {
    marked?: boolean;
    selected?: boolean;
    customStyles?: {
      container?: ViewStyle;
      text?: TextStyle;
    };
  };
  state?: "selected" | "disabled" | "today" | "";
  onDayPress?: (date: DateData) => void; // DateData 타입으로 변경
}
export default function CalendarScreen() {
  const [checkDate, setCheckDate] = useState<string>("");
  const [isModalVisible, setIsModalVisible] = useState<boolean>(false);
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

  // 기본 마커 스타일을 객체로 정의
  const scheduleStyle = {
    marked: true,
    customStyles: {
      container: {
        flexDirection: "column",
        alignItems: "center",
        height: 60,
      },
      wrapper: {
        backgroundColor: Colors.sublight,
        padding: 4,
        borderRadius: 4,
        marginTop: 4,
      },
      scheduleText: {
        fontSize: 12,
        color: Colors.white,
      },
    },
  };

  const CalendarView = ({ checkDate, setCheckDate }: CalenderType) => {
    const markedDates: Record<string, any> = {
      "2024-12-06": { ...scheduleStyle },
      "2024-12-07": { ...scheduleStyle },
      "2024-12-08": { ...scheduleStyle },
    };

    const markedSelectedDates = {
      ...markedDates,
      [checkDate]: {
        selected: true,
        marked: markedDates[checkDate]?.marked, // 기존 marked 속성 유지
        selectedColor: "white", // 배경색을 흰색으로
        selectedTextColor: Colors.black, // 텍스트 색상
        customStyles: {
          container: {
            borderRadius: 5,
            borderWidth: 1, // border 두께
            borderColor: Colors.sublight, // border 색상
            width: "100%",
          },
        },
      },
    };

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
              height: 60,
              alignItems: "center",
              width: "100%",
            },
            // 선택된 날짜에 대한 스타일
            marking?.selected && {
              borderRadius: 5,
              borderWidth: 1,
              borderColor: Colors.sublight,
            },
          ]}
        >
          <Text
            style={{
              color: state === "disabled" ? "gray" : "black",
            }}
          >
            {date.day}
          </Text>
          {marking?.marked && (
            <View
              style={{
                backgroundColor: Colors.sublight,
                padding: 4,
                borderRadius: 4,
                marginTop: 4,
                width: "80%",
              }}
            >
              <Text
                style={{
                  fontSize: 12,
                  color: "white",
                  textAlign: "center",
                }}
              >
                일정
              </Text>
            </View>
          )}
        </Pressable>
      );
    };
    return (
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
          calendarBackground: "#ffffff",
          textSectionTitleColor: Colors.black,
          textMonthFontSize: 18,

          // 선택된 날짜 스타일
          selectedDayBackgroundColor: Colors.main,
          selectedDayTextColor: "#ffffff",

          // 오늘 날짜 스타일
          todayTextColor: Colors.subbold,
        }}
        dayComponent={(props: DayProps) => (
          <CustomDay
            {...props}
            onDayPress={(day: DateData) => {
              console.log(day);
              setCheckDate(day.dateString);
              setIsModalVisible(true);
            }}
          />
        )}
        markedDates={markedSelectedDates}
        markingType="custom"
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
      <View className="w-full h-[70%] bg-slate-200">
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
