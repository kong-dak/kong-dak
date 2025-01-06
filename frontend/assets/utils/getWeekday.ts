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
