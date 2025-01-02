import { AppText } from "@/components/common/AppText";
import { Colors } from "@/constants/Colors";
import { AntDesign } from "@expo/vector-icons";
import { router } from "expo-router";
import { useState } from "react";
import {
  View,
  StyleSheet,
  TouchableOpacity,
  Image,
  ScrollView,
} from "react-native";

export default function DiaryWriteScreen() {
  const url = require("../../assets/images/diary-write-sample.png");
  const [imageUri, setImageUri] = useState(url); // 다이어리 이미지 url
  return (
    <View style={styles.container}>
      {/* 일기 메인 */}
      <View
        className="h-[5%] flex flex-row justify-between items-center p-2"
        style={{ borderColor: Colors.gray }}
      >
        <View className="flex flex-row items-center">
          <TouchableOpacity onPress={() => router.back()}>
            <AntDesign
              className="me-1"
              name="left"
              size={16}
              color={Colors.main}
            />
          </TouchableOpacity>

          <AppText className="text-xl">2024년 11월 21일</AppText>
          <AntDesign
            className="mx-1"
            name="cloudo"
            size={20}
            color={Colors.black}
          />
          <AntDesign
            className="mx-1"
            name="smileo"
            size={20}
            color={Colors.black}
          />
        </View>

        <View className="flex flex-row items-center">
          <TouchableOpacity onPress={() => router.push("/write")}>
            <AppText className="text-xl">수정</AppText>
          </TouchableOpacity>
          <AppText className="text-xl ms-4" style={{ color: Colors.red1 }}>
            삭제
          </AppText>
        </View>
      </View>
      {/* 이미지 */}
      <View className={`w-full ${imageUri ? "h-[30%]" : "h-0"} p-4`}>
        {imageUri && (
          <Image source={url} className="w-full h-full" resizeMode="cover" />
        )}
      </View>
      {/* 내용 */}
      <ScrollView className={`${imageUri ? "h-[65%]" : "h-[95]"}`}>
        <AppText className="px-4 text-xl" style={{ lineHeight: 22 }}>
          은수 {"\n"}현재 오전 3시 00분..제길.. 그냥 레이아웃만 대충 짜고
          잘려고했는데 왜 굳이 디자인 이쁘게 짜고싶어서 찾고하고 찾고하고
          했을까.{"\n"}더 하면 내일 오후 2시에 일어날 거 같아.,.,. 일단 오늘은
          여기서 잔다.,.{"\n\n"}창호형이랑 현춘이.. 창호형은 잘 하고 있을까...
          ERD양 디지게 많아 보이 던데 다 못했겠지...? 믿고 잔다..{"\n"}
          {"\n"} 현춘이도 애인이랑 노느라 API 다 안했을거라 믿는다.
          {"\n"}
          {"\n"}사랑한다 얘들아..은수 {"\n"}현재 오전 3시 00분..제길.. 그냥
          레이아웃만 대충 짜고 잘려고했는데 왜 굳이 디자인 이쁘게 짜고싶어서
          찾고하고 찾고하고 했을까.{"\n"}더 하면 내일 오후 2시에 일어날 거
          같아.,.,. 일단 오늘은 여기서 잔다.,.{"\n\n"}창호형이랑 현춘이..
          창호형은 잘 하고 있을까... ERD양 디지게 많아 보이 던데 다 못했겠지...?
          믿고 잔다..{"\n"}
          {"\n"} 현춘이도 애인이랑 노느라 API 다 안했을거라 믿는다.
          {"\n"}
          {"\n"}사랑한다 얘들아..
        </AppText>
      </ScrollView>
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
});
