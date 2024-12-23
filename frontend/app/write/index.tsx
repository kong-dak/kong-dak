import { AppText } from "@/components/common/AppText";
import { Colors } from "@/constants/Colors";
import { AntDesign } from "@expo/vector-icons";
import { router } from "expo-router";
import { useState } from "react";
import {
  View,
  StyleSheet,
  TextInput,
  Image,
  TouchableOpacity,
} from "react-native";

export default function DiaryWriteScreen() {
  const url = require("../../assets/images/diary-write-sample.png");
  const [imageUri, setImageUri] = useState(url); // 다이어리 이미지 url
  return (
    <View style={styles.container}>
      {/* 일기 메인 */}
      <View
        className="h-[5%] flex flex-row justify-between items-center border-b p-2"
        style={{ borderColor: Colors.gray }}
      >
        <AppText className="text-base">취소</AppText>
        <AppText className="text-xl">일기</AppText>
        <TouchableOpacity onPress={() => router.push("/(tabs)/diary")}>
          <AppText className="text-base">작성</AppText>
        </TouchableOpacity>
      </View>
      {/* 날씨 기분 날짜 */}
      <View className="h-[5%] flex flex-row justify-between items-center px-2">
        <View className="flex flex-row items-center my-2">
          <View className="flex flex-row items-center me-4">
            <AppText className="text-base me-2">날씨:</AppText>
            <AntDesign
              className="me-1"
              name="cloudo"
              size={16}
              color={Colors.black}
            />
            <AntDesign name="down" size={16} color={Colors.black} />
          </View>
          <View className="flex flex-row items-center">
            <AppText className="text-base me-2">기분:</AppText>
            <AntDesign
              className="me-1"
              name="smileo"
              size={16}
              color={Colors.black}
            />
            <AntDesign name="down" size={16} color={Colors.black} />
          </View>
        </View>
        <View
          className="flex items-center border-b"
          style={{ borderColor: Colors.gray }}
        >
          <AppText className="text-base">2024년 11월 21일</AppText>
        </View>
      </View>
      <View className="h-[85%]">
        {/* 일기 작성 */}
        <View
          className={`${imageUri ? "h-[70%]" : "h-full"} flex justify-between`}
        >
          <TextInput
            className="text-start"
            style={[styles.TextInput, { color: Colors.main, outline: "none" }]}
            placeholder="일기를 작성해주세요."
            placeholderTextColor={Colors.gray}
          />
          <View className=" flex justify-end items-end">
            <AppText
              className=" text-center my-2"
              style={{ color: Colors.gray }}
            >
              0 / 1000자
            </AppText>
          </View>
        </View>

        {/* 이미지 */}
        <View className={`w-full ${imageUri ? "h-[30%]" : "h-0"} p-4`}>
          {imageUri && (
            <Image source={url} className="w-full h-full" resizeMode="cover" />
          )}
        </View>
      </View>

      {/* 사진탭 */}
      <View
        className="h-[5%] flex items-start justify-center ps-2 border-t"
        style={{ borderColor: Colors.black }}
      >
        <AntDesign name="picture" size={26} color="black" />
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
    padding: 4,
    marginBottom: 2, // 밑줄과의 간격
    outline: "none",
    width: "110%", // 입력창 전체 너비 사용
    outlineColor: "#929292",
    fontSize: 16,
    fontFamily: "GowunDodum-Regular",
  },
});
