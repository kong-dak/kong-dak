import { AppButton } from "@/components/common/AppButton";
import { AppText } from "@/components/common/AppText";
import CustomCalendarMini from "@/components/ui/CustomCalendarMini";
import TimePicker from "@/components/ui/TimePicker";
import MapScreen from "@/components/ui/MapScreen";
import MapSearchBar from "@/components/ui/MapSearchBar";
import { Colors } from "@/constants/Colors";
import { AntDesign, Fontisto } from "@expo/vector-icons";
import { router } from "expo-router";
import { useEffect, useState } from "react";
import { View, StyleSheet, TextInput, Switch, Pressable } from "react-native";
import { SearchResponse } from "@/assets/types/map/mapModels";
import { useSearch } from "@/hooks/useSearch";

export default function CalenderregistScreen() {
  const url = require("../../assets/images/diary-write-sample.png");
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

  useEffect(() => {
    if (searchQuery) {
      handleSearch(searchQuery);
    }
  }, [searchQuery]);

  // 검색 결과를 콘솔에 출력
  useEffect(() => {
    console.log("검색 결과:", searchResults);
  }, [searchResults]);

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
              <AppText className="text-xl" color={Colors.lightgray1}>
                11월 21일 (목)
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
              <AppText className="text-xl" color={Colors.lightgray1}>
                오전 9시
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
              <AppText className="text-xl" color={Colors.lightgray1}>
                11월 21일 (목)
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
              <AppText className="text-xl" color={Colors.lightgray1}>
                오전 9시
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
            currentDay={"2025-01-06"}
            isCalendarStart={isCalendarStart}
            isCalendarEnd={!isCalendarStart}
          />
        ) : null}
        {isTimePickerStart || isTimePickerEnd ? (
          <TimePicker
            itemHeight={36}
            onTimeChange={(time) => {
              console.log(time);
            }}
          />
        ) : null}

        {/* 지도 검색 */}

        <MapSearchBar onSearch={(query) => setSearchQuery(query)} />
        {/* 지도 */}
        <MapScreen searchResults={searchResults} />
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
          style={{ width: "45%" }}
          outline={true}
        />
        <AppButton
          text="일정 등록"
          type="sublight"
          onPress={() => router.back()}
          style={{ width: "45%" }}
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
