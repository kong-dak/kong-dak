export const getWeekday = (dateString: string, language: "ko" | "en") => {
  // 날짜 문자열을 Date 객체로 변환
  const date = new Date(dateString);

  // 요일을 한글로 반환
  const weekdaysKo = [
    "일요일",
    "월요일",
    "화요일",
    "수요일",
    "목요일",
    "금요일",
    "토요일",
  ];
  const weekdaysEn = ["SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"];
  if (language === "ko") {
    return weekdaysKo[date.getDay()];
  } else {
    return weekdaysEn[date.getDay()];
  }
};
export const getDayOfWeek = (dateString: string) => {
  // 날짜 문자열을 Date 객체로 변환
  const date = new Date(dateString);

  // 한글 요일 배열
  const weekDays = [
    "일요일",
    "월요일",
    "화요일",
    "수요일",
    "목요일",
    "금요일",
    "토요일",
  ];

  // getDay()는 0(일요일)부터 6(토요일)까지의 숫자를 반환
  return weekDays[date.getDay()];
};
