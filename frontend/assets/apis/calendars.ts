import localAxios from "../utils/http-common";

const local = localAxios();

/**월별 일정 조회 */
export async function monthlySchedules(datetime: string) {
  return await local.get(`/api/calendars/schedules/monthly`, {
    params: {
      datetime: datetime,
    },
  });
}

/**일별 일정 조회 */
export async function dailySchedules(datetime: string) {
  return await local.get(`/api/calendars/schedules/daily`, {
    params: {
      datetime: datetime,
    },
  });
}

/**일정 상세 조회 */
export async function scheduleDetail(calendarId: number, scheduleId: number) {
  return await local.get(
    `/api/calendars/${calendarId}/schedules/${scheduleId}`
  );
}

/**일정 추가 */
export async function createSchedule(calendarId: number) {
  return await local.post(`/api/calendars/${calendarId}/schedules`);
}

/**일정 수정 */
export async function modifySchedule(calendarId: number, scheduleId: number) {
  return await local.patch(
    `/api/calendars/${calendarId}/schedules/${scheduleId}`
  );
}

/**일정 삭제 */
export async function deleteSchedule(calendarId: number, scheduleId: number) {
  return await local.delete(
    `/api/calendars/${calendarId}/schedules/${scheduleId}`
  );
}
