import {
  CalenderType,
  DayProps,
  MarkedDatesType,
  ScheduleStyleProps,
} from "@/assets/types/type";
import { Colors } from "@/constants/Colors";
import { Feather } from "@expo/vector-icons";
import { SetStateAction, useEffect, useState } from "react";
import { View, Text, ViewStyle, TextStyle, Pressable } from "react-native";
import { Calendar, DateData, LocaleConfig } from "react-native-calendars";

interface CustomCalendarMiniProps {
  currentDay: string;
  setState: React.Dispatch<React.SetStateAction<string>>;
}

export default function CustomCalendarDiary({
  currentDay,
  setState,
}: CustomCalendarMiniProps) {
  const [checkDate, setCheckDate] = useState<string>("");
  const [selectedDay, setSelectedDay] = useState<string>(currentDay);

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

  const [markedDates, setMarkedDates] = useState<MarkedDatesType>({});

  useEffect(() => {
    if (currentDay) {
      setMarkedDates({ [currentDay]: { ...scheduleStyle } });
    }
  }, []);
  useEffect(() => {
    // console.log(markedDates);
  }, [markedDates]);
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

  const changeSelectedDays = (day: string) => {
    // 새로운 날짜만 마킹하도록 수정
    setMarkedDates({
      [day]: { ...scheduleStyle },
    });
    setState(day);
  };

  const CalendarView = ({ checkDate, setCheckDate }: CalenderType) => {
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
              height: 50,
              alignItems: "center",
              width: "100%",
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
                borderRadius: "100%",
                marginTop: 8,
                width: "50%",
                aspectRatio: 1 / 1,
              }}
            ></View>
          )}
        </Pressable>
      );
    };
    return (
      <Calendar
        current={currentDay}
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
              //   console.log(day);
              changeSelectedDays(day.dateString);
              setCheckDate(day.dateString);
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
    <View className="w-full h-[70%] bg-slate-200">
      <CalendarView checkDate={checkDate} setCheckDate={setCheckDate} />
    </View>
  );
}
