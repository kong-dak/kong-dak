import {
  MarkedDateProps,
  SchedulePeriod,
} from "../types/calendar/calendarModels";

// 기간 데이터를 markedDates 형식으로 변환
export const generateMarkedDates = (
  schedulePeriods: SchedulePeriod[],
  year: string,
  month: string
) => {
  const markedDates: Record<string, MarkedDateProps> = {};
  const sundays = getSundayDates(year, month);
  schedulePeriods.forEach((period, index) => {
    const start = new Date(period.startTime);
    const end = new Date(period.endTime);
    let periodsLength = 0;
    const startDay = start.toISOString().split("T")[0];

    if (markedDates[startDay]) {
      periodsLength = markedDates[startDay].periods.length;
    }
    for (
      let date = start;
      date <= end;
      date = new Date(date.setDate(date.getDate() + 1))
    ) {
      const dateStr = date.toISOString().split("T")[0];
      if (!markedDates[dateStr]) {
        markedDates[dateStr] = {
          periods: [],
        };
      }
      if (sundays.includes(dateStr)) {
        periodsLength = markedDates[dateStr].periods.length;
      }

      markedDates[dateStr].periods.push({
        scheduleId: period.scheduleId,
        startTime: period.startTime,
        endTime: period.endTime,
        color: period.color,
        title: period.title,
        idx: periodsLength,
        myLatitude: 123123,
        myLongitude: 123123,
      });
    }
  });

  return markedDates;
};

/** 정렬이 필요한 일요일을 추출해주는 함수 매개변수: (year:string, month:string) */
export const getSundayDates = (year: string, month: string) => {
  const baseDate = new Date(Number(year), Number(month) - 1, 15);
  const sundays = [];

  const startDate = new Date(baseDate);
  startDate.setDate(baseDate.getDate() - 21);
  const endDate = new Date(baseDate);
  endDate.setDate(baseDate.getDate() + 21);

  const currentDate = new Date(startDate);
  currentDate.setDate(currentDate.getDate() + ((7 - currentDate.getDay()) % 7));

  while (currentDate <= endDate) {
    const formattedDate = currentDate.toISOString().split("T")[0];
    sundays.push(formattedDate);

    currentDate.setDate(currentDate.getDate() + 7);
  }
  return sundays;
};

/** 날짜를 변환 */
export const getTodayDates = () => {
  const date = new Date();
  const year = date.getFullYear();
  const month = date.getMonth() + 1;
  const day = date.getDate();
  const hour = date.getHours();
  const minute = date.getMinutes();
  return { year, month, day, hour, minute };
};
