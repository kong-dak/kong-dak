import { AppButton } from "@/components/common/AppButton";
import { AppText } from "@/components/common/AppText";
import CustomCalendarMini from "@/components/ui/CustomCalendarMini";
import TimePicker from "@/components/ui/TimePicker";
import MapScreen from "@/components/ui/MapScreen";
import MapSearchBar from "@/components/ui/MapSearchBar";
import { Colors } from "@/constants/Colors";
import { AntDesign, Fontisto } from "@expo/vector-icons";
import { router, useLocalSearchParams } from "expo-router";
import { useEffect, useState } from "react";
import {
  View,
  StyleSheet,
  TextInput,
  Switch,
  Pressable,
  Alert,
} from "react-native";
import { SearchResponse } from "@/assets/types/map/mapModels";
import { useSearch } from "@/hooks/useSearch";
import PlaceInfo from "@/components/ui/PlaceInfo";
import {
  CalendarDetail,
  CategoryEnum,
  SchedulePeriod,
} from "@/assets/types/calendar/calendarModels";
import { getTodayDates } from "@/assets/utils/calendar";
import {
  createSchedule,
  deleteSchedule,
  modifySchedule,
} from "@/assets/apis/calendars";

export default function CalenderregistScreen() {
  const url = require("../../assets/images/diary-write-sample.png");
  const today = getTodayDates();
  const initialDay =
    today.year +
    "-" +
    today.month.toString().padStart(2, "0") +
    "-" +
    today.day.toString().padStart(2, "0");
  const initialTime =
    today.hour.toString().padStart(2, "0") +
    ":" +
    today.minute.toString().padStart(2, "0");
  const [scheduleTogether, setScheduleTogether] = useState<boolean>(false);
  const [showOnly, setShowOnly] = useState<boolean>(false);
  const [isCalendarStart, setIsCalendarStart] = useState<boolean>(false);
  const [isCalendarEnd, setIsCalendarEnd] = useState<boolean>(false);
  const [isTimePickerStart, setIsTimePickerStart] = useState<boolean>(false);
  const [isTimePickerEnd, setIsTimePickerEnd] = useState<boolean>(false);
  const [searchQuery, setSearchQuery] = useState<string>("");
  const [searchResults, setSearchResults] = useState<
    SearchResponse["data"]["documents"]
  >([]);
  const { handleSearch } = useSearch(searchResults, setSearchResults);
  const params = useLocalSearchParams();
  const {
    scheduleId,
    title,
    color,
    startTime,
    endTime,
    idx,
    myLatitude,
    myLongitude,
    category,
    type,
  } = params;
  const [currentLatitude, setCurrentLatitude] = useState<number>(
    Number(myLatitude)
  );
  const [currentLongitude, setCurrentLongitude] = useState<number>(
    Number(myLongitude)
  );
  const [editScheduleId, setEditScheduleId] = useState<number>(
    Number(scheduleId)
  );

  const [calendarDetail, setCalendarDetail] = useState<CalendarDetail>({
    title: "",
    startTime: startTime + "T" + initialTime,
    endTime: endTime + "T" + initialTime,
    description: "",
    category: "PERSONAL",
    emoji: "",
  });

  const [calendarStartDay, setCalendarStartDay] = useState<string>(
    startTime + ""
  );
  const [calendarStartTime, setCalendarStartTime] =
    useState<string>(initialTime);
  const [calendarEndDay, setCalendarEndDay] = useState<string>(endTime + "");
  const [calendarEndTime, setCalendarEndTime] = useState<string>(initialTime);

  const [selectedPlace, setSelectedPlace] = useState<
    SearchResponse["data"]["documents"][0] | null
  >(null);

  useEffect(() => {
    if (params.type === "EDIT") {
      setCalendarDetail({
        title: title as string,
        startTime: startTime as string,
        endTime: endTime as string,
        description: "",
        category: category as CategoryEnum,
        emoji: "",
      });

      const startString = startTime as string;
      setCalendarStartDay(startString.split("T")[0]);
      setCalendarStartTime(startString.split("T")[1]);
      const endString = endTime as string;
      setCalendarEndDay(endString.split("T")[0]);
      setCalendarEndTime(endString.split("T")[1]);
    }
  }, []);

  useEffect(() => {
    if (searchQuery) {
      handleSearch(searchQuery, {
        x: currentLongitude + "",
        y: currentLatitude + "",
      });
    }
  }, [searchQuery]);

  // 검색 결과를 콘솔에 출력
  useEffect(() => {
    console.log("검색 결과:", searchResults);
  }, [searchResults]);

  useEffect(() => {
    if (scheduleTogether) {
      setCalendarDetail((prev) => ({ ...prev, category: "SHARED" }));
    } else if (!scheduleTogether && showOnly) {
      setCalendarDetail((prev) => ({ ...prev, category: "PRIVATE" }));
    } else if (!scheduleTogether && !showOnly) {
      setCalendarDetail((prev) => ({ ...prev, category: "PERSONAL" }));
    }
  }, [scheduleTogether, showOnly]);

  useEffect(() => {
    setCalendarDetail((prev) => ({
      ...prev,
      startTime: calendarStartDay + "T" + calendarStartTime,
    }));
  }, [calendarStartDay, calendarStartTime]);
  useEffect(() => {
    setCalendarDetail((prev) => ({
      ...prev,
      endTime: calendarEndDay + "T" + calendarEndTime,
    }));
  }, [calendarEndDay, calendarEndTime]);

  const handleCameraIdle = (latitude: number, longitude: number) => {
    setCurrentLatitude(latitude);
    setCurrentLongitude(longitude);
  };

  const onChangeTitle = (e: string) => {
    if (e.length > 20) {
      Alert.alert("알림", "20글자 이하로 작성해주시길 바랍니다", [
        { text: "확인" },
      ]);
    } else {
      setCalendarDetail((prev) => ({ ...prev, title: e }));
    }
  };

  const writeCalendar = async () => {
    console.log(calendarDetail);
    if (calendarDetail) {
      if (params.type === "POST") {
        await createSchedule(calendarDetail).then((res) => {
          console.log("생성합니다.", res.data);
          router.navigate("/(tabs)/calendar");
        });
      } else if (params.type === "EDIT") {
        console.log(editScheduleId);
        await modifySchedule(editScheduleId, calendarDetail).then((res) => {
          console.log("수정합니다.", res.data);
          router.navigate("/(tabs)/calendar");
        });
      } else {
        Alert.alert("알림", "죄송합니다. 오류가 발생하였습니다", [
          { text: "확인" },
        ]);
        router.navigate("/(tabs)");
      }
    }
  };
  const deleteCalendar = async () => {
    if (calendarDetail) {
      if (params.type === "EDIT") {
        console.log(editScheduleId);
        await deleteSchedule(editScheduleId).then((res) => {
          console.log("삭제되었습니다.", res.data);
          router.navigate("/(tabs)/calendar");
        });
      } else {
        Alert.alert("알림", "죄송합니다. 오류가 발생하였습니다", [
          { text: "확인" },
        ]);
        router.navigate("/(tabs)");
      }
    }
  };

  const timeFormatting = (time: string) => {
    return Number(time.split(":")[0]) < 12
      ? "오전 " + Number(time.split(":")[0]) + "시 " + time.split(":")[1] + "분"
      : "오후 " +
          (Number(time.split(":")[0]) === 12
            ? Number(time.split(":")[0])
            : Number(time.split(":")[0]) - 12) +
          "시 " +
          time.split(":")[1] +
          "분";
  };

  return (
    <View style={styles.container}>
      {/* 날씨 기분 날짜 */}
      <View className="h-[90%] flex px-2">
        <View className="w-full flex flex-row justify-between items-center">
          <TextInput
            className="w-[90%] text-start ms-4"
            style={[styles.TextInput, { color: Colors.black }]}
            placeholder="알림 제목"
            placeholderTextColor={Colors.gray}
            value={calendarDetail.title}
            onChangeText={(e) => {
              onChangeTitle(e);
            }}
          />
          <AntDesign
            className="w-[10%]"
            name="smileo"
            size={24}
            color={Colors.black}
          />
        </View>
        <View
          className="w-full border-b"
          style={{ borderColor: Colors.black }}
        />
        <View className="w-full flex flex-row justify-between items-center my-2">
          <AppText className="text-xl" color={Colors.black}>
            함께 일정
          </AppText>
          <Switch
            trackColor={{ false: Colors.lightgray2, true: Colors.main }}
            thumbColor={"#f4f3f4"}
            ios_backgroundColor="#3e3e3e"
            // 함수 호출
            onValueChange={() => {
              setScheduleTogether(!scheduleTogether);
            }}
            value={scheduleTogether}
            style={{ transform: [{ scaleX: 1.5 }, { scaleY: 1.5 }] }}
          />
        </View>
        <View className="w-full flex flex-row justify-between items-center my-2">
          <AppText className="text-xl" color={Colors.lightgray1}>
            나만 보기
          </AppText>
          <Switch
            trackColor={{ false: Colors.lightgray2, true: Colors.main }}
            thumbColor={"#f4f3f4"}
            ios_backgroundColor="#3e3e3e"
            // 함수 호출
            onValueChange={() => {
              setShowOnly(!showOnly);
            }}
            value={showOnly}
            style={{ transform: [{ scaleX: 1.5 }, { scaleY: 1.5 }] }}
          />
        </View>
        <View
          className="w-full border-b"
          style={{ borderColor: Colors.black }}
        />
        <View className="w-full flex flex-row justify-between items-center my-2">
          <View className="flex flex-col items-center justify-center">
            <Pressable
              onPress={() => {
                setIsCalendarStart(!isCalendarStart);
                setIsCalendarEnd(false);
                setIsTimePickerStart(false);
                setIsTimePickerEnd(false);
              }}
            >
              <AppText
                className="text-xl py-1 px-1 rounded-md"
                color={Colors.lightgray1}
                style={
                  isCalendarStart ? { backgroundColor: Colors.lightgray2 } : {}
                }
              >
                {calendarStartDay}
              </AppText>
            </Pressable>

            <Pressable
              onPress={() => {
                setIsCalendarStart(false);
                setIsCalendarEnd(false);
                setIsTimePickerStart(!isTimePickerStart);
                setIsTimePickerEnd(false);
              }}
            >
              <AppText
                className="text-xl py-1 px-1 rounded-md"
                color={Colors.lightgray1}
                style={
                  isTimePickerStart
                    ? { backgroundColor: Colors.lightgray2 }
                    : {}
                }
              >
                {timeFormatting(calendarStartTime)}
              </AppText>
            </Pressable>
          </View>

          <AntDesign name="arrowright" size={24} color={Colors.black} />
          <View className="flex flex-col items-center justify-center">
            <Pressable
              onPress={() => {
                setIsCalendarStart(false);
                setIsCalendarEnd(!isCalendarEnd);
                setIsTimePickerStart(false);
                setIsTimePickerEnd(false);
              }}
            >
              <AppText
                className="text-xl py-1 px-1 rounded-md"
                color={Colors.lightgray1}
                style={
                  isCalendarEnd ? { backgroundColor: Colors.lightgray2 } : {}
                }
              >
                {calendarEndDay}
              </AppText>
            </Pressable>
            <Pressable
              onPress={() => {
                setIsCalendarStart(false);
                setIsCalendarEnd(false);
                setIsTimePickerStart(false);
                setIsTimePickerEnd(!isTimePickerEnd);
              }}
            >
              <AppText
                className="text-xl py-1 px-1 rounded-md"
                color={Colors.lightgray1}
                style={
                  isTimePickerEnd ? { backgroundColor: Colors.lightgray2 } : {}
                }
              >
                {timeFormatting(calendarEndTime)}
              </AppText>
            </Pressable>
          </View>
        </View>
        <View
          className="w-full border-b"
          style={{ borderColor: Colors.black }}
        />

        {/* 캘린더 관련 isCalendarEnd의 값이 !isCalendarStart인 이유는 하나만 true 상태로 놔두기 위해.*/}
        {isCalendarStart || isCalendarEnd ? (
          <CustomCalendarMini
            startingDay={calendarDetail.startTime.split("T")[0]}
            endingDay={calendarDetail.endTime.split("T")[0]}
            isCalendarStart={isCalendarStart}
            isCalendarEnd={!isCalendarStart}
            setStart={setCalendarStartDay}
            setEnd={setCalendarEndDay}
          />
        ) : null}
        {isTimePickerStart || isTimePickerEnd ? (
          <TimePicker
            itemHeight={36}
            onTimeChange={(time) => {
              console.log(time);
            }}
            isTimePickerStart={isTimePickerStart}
            isTimePickerEnd={isTimePickerEnd}
            startTime={calendarStartTime}
            endTime={calendarEndTime}
            setStartTime={setCalendarStartTime}
            setEndTime={setCalendarEndTime}
          />
        ) : null}

        {/* 지도 검색 */}
        <MapSearchBar onSearch={(query) => setSearchQuery(query)} />

        {/* 지도 */}
        <MapScreen
          myLatitude={Number(params.myLatitude)}
          myLongitude={Number(params.myLongitude)}
          searchResults={searchResults}
          onCameraIdle={handleCameraIdle}
          selectedPlace={selectedPlace}
          setSelectedPlace={setSelectedPlace}
        />

        {/* 장소 정보 표시 */}
        <View className="h-[24%]">
          <PlaceInfo
            searchResults={searchResults}
            selectedPlace={selectedPlace}
            setSelectedPlace={setSelectedPlace}
          />
        </View>

        <View
          className="w-full flex flex-row items-center mt-4 border-2 rounded-md justify-between"
          style={{ borderColor: Colors.main }}
        >
          <TextInput
            className="w-[80%] text-start ms-2"
            style={[styles.TextInput, { color: Colors.black, fontSize: 18 }]}
            placeholder="메모"
            placeholderTextColor={Colors.gray}
          />
        </View>
      </View>

      {/* 일기 메인 */}
      <View
        className="h-[10%] flex flex-row justify-around items-center border-b p-2"
        style={{ borderColor: Colors.gray }}
      >
        <AppButton
          text="취소하기"
          type="main"
          onPress={() => router.back()}
          style={type === "EDIT" ? { width: "32%" } : { width: "45%" }}
          outline={true}
        />
        <AppButton
          text="일정등록"
          type="sublight"
          onPress={() => writeCalendar()}
          style={type === "EDIT" ? { width: "32%" } : { width: "45%" }}
        />
        <AppButton
          text="삭제"
          type="red"
          outline={true}
          onPress={() => deleteCalendar()}
          style={type === "EDIT" ? { width: "32%" } : { display: "none" }}
        />
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
  TextInput: {
    padding: 8,
    marginBottom: 2, // 밑줄과의 간격
    outline: "none",
    outlineColor: "#929292",
    fontSize: 20,
    fontFamily: "GowunDodum-Regular",
  },
});
