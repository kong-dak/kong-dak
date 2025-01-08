import {
  CalenderType,
  DayProps,
  MarkedDatesType,
  ScheduleStyleProps,
} from "@/assets/types/type";
import { Colors } from "@/constants/Colors";
import { Feather } from "@expo/vector-icons";
import { useEffect, useState } from "react";
import { View, Text, ViewStyle, TextStyle, Pressable } from "react-native";
import { Calendar, DateData, LocaleConfig } from "react-native-calendars";

interface CustomCalendarMiniProps {
  currentDay: string;
  isCalendarStart: boolean;
  isCalendarEnd: boolean;
}

export default function CustomCalendarMini({
  currentDay,
  isCalendarStart,
  isCalendarEnd,
}: CustomCalendarMiniProps) {
  const [checkDate, setCheckDate] = useState<string>("");
  const [startDay, setStartDay] = useState<string>(currentDay);
  const [endDay, setEndDay] = useState<string>(currentDay);
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

  const [markedDates, setMarkedDates] = useState<MarkedDatesType>({
    "2025-01-08": { ...scheduleStyle },
  });

  useEffect(() => {
    console.log(currentDay);
    setMarkedDates({ currentDay: { ...scheduleStyle } });
  }, []);
  useEffect(() => {
    console.log(markedDates);
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

  //Date 뽑기
  const changeDate = (changeDay: Date) => {
    const year = changeDay.getFullYear();
    const month = String(changeDay.getMonth() + 1).padStart(2, "0");
    const day = String(changeDay.getDate()).padStart(2, "0");
    const dateString = `${year}-${month}-${day}`;
    return dateString;
  };
  const addCalendarDay = (start: string, end: string, type: string) => {
    const startDate = new Date(start);
    const endDate = new Date(end);
    const newDates: MarkedDatesType = {};

    const currentDate = new Date(startDate);

    while (currentDate.getTime() <= endDate.getTime()) {
      // YYYY-MM-DD 형식으로 변환
      const year = currentDate.getFullYear();
      const month = String(currentDate.getMonth() + 1).padStart(2, "0");
      const day = String(currentDate.getDate()).padStart(2, "0");
      const dateString = `${year}-${month}-${day}`;

      newDates[dateString] = { ...scheduleStyle };

      // 다음 날짜로 이동
      currentDate.setDate(currentDate.getDate() + 1);
    }

    //startDay조정
    if (startDay > changeDate(startDate)) {
      setStartDay(changeDate(startDate));
    }
    if (endDay < changeDate(endDate)) {
      setEndDay(changeDate(endDate));
    }

    setMarkedDates((prev) => ({
      ...prev,
      ...newDates,
    }));
  };
  const removeCalendarDay = (start: string, end: string, type: string) => {
    console.log(start + " 부터 " + end + " 까지 삭제합니다.");
    const startDate = new Date(start);
    const endDate = new Date(end);

    // 현재 markedDates를 복사
    const updatedDates: MarkedDatesType = { ...markedDates };
    const currentDate = new Date(startDate);

    while (currentDate.getTime() <= endDate.getTime()) {
      // YYYY-MM-DD 형식으로 변환
      const year = currentDate.getFullYear();
      const month = String(currentDate.getMonth() + 1).padStart(2, "0");
      const day = String(currentDate.getDate()).padStart(2, "0");
      const dateString = `${year}-${month}-${day}`;

      // 해당 날짜 삭제
      if (updatedDates[dateString]) {
        delete updatedDates[dateString];
      }

      //startDay조정
      if (type === "start") {
        const setupDay = changeDate(endDate);
        setStartDay(setupDay);
        if (endDay < changeDate(endDate)) {
          setEndDay(changeDate(endDate));
        }
        updatedDates[setupDay] = { ...scheduleStyle };
      } else {
        const setupDay = changeDate(startDate);
        setEndDay(setupDay);
        if (startDay > changeDate(startDate)) {
          setStartDay(changeDate(startDate));
        }
        updatedDates[setupDay] = { ...scheduleStyle };
      }
      // 다음 날짜로 이동
      currentDate.setDate(currentDate.getDate() + 1);
    }

    // 업데이트된 날짜들로 상태 변경
    setMarkedDates(updatedDates);
  };
  const changeSelectedDays = (day: string) => {
    // 시작 기간 설정을 눌렀을 때
    if (isCalendarStart) {
      console.log("시작 날짜." + day);
      console.log("종료 날짜." + startDay);
      //시작 날짜보다 이전 구간을 눌렀을 때 이전 구간들을 추가.
      if (day < startDay) {
        addCalendarDay(day, startDay, "start");
      }
      //시작 날짜보다 이후 구간을 눌렀을 때 사이의 날짜들을 삭제.
      else if (day > startDay) {
        removeCalendarDay(startDay, day, "start");
      }
    }
    // 종료 기간 설정을 눌렀을 때
    else {
      console.log("시작 날짜." + day);
      console.log("종료 날짜." + endDay);
      //종료 날짜보다 이후 구간을 눌렀을 때 이전 구간들을 추가.
      if (day >= endDay) {
        addCalendarDay(endDay, day, "end");
      }
      //종료 날짜보다 이전 구간을 눌렀을 때 사이의 날짜들을 삭제.
      else if (day < endDay) {
        removeCalendarDay(day, endDay, "end");
      }
    }
    // setMarkedDates((prev) => {
    //   // 날짜가 이미 존재하는지 확인
    //   if (prev[day]) {
    //     // 존재하면 해당 날짜를 제외한 새로운 객체 생성
    //     const { [day]: _, ...rest } = prev;
    //     return rest;
    //   } else {
    //     // 존재하지 않으면 새로운 날짜 추가
    //     return {
    //       ...prev,
    //       [day]: { ...scheduleStyle },
    //     };
    //   }
    // });
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
                선택
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
