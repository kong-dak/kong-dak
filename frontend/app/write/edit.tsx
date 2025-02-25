import { deleteDiary } from "@/assets/apis/diary";
import { AppText } from "@/components/common/AppText";
import { Colors } from "@/constants/Colors";
import { AntDesign } from "@expo/vector-icons";
import { router, useLocalSearchParams } from "expo-router";
import { useState } from "react";
import {
  View,
  StyleSheet,
  TouchableOpacity,
  Image,
  ScrollView,
  Pressable,
} from "react-native";

export default function DiaryEditScreen() {
  const { diaryId, datetime, content, weather, photos } =
    useLocalSearchParams();
  // const url = require("../../assets/images/diary-write-sample.png");
  // const [imageUri, setImageUri] = useState(url); // 다이어리 이미지 url

  const onClickDeleteDiary = async () => {
    await deleteDiary(Number(diaryId)).then((res) => {
      console.log(res.data.data);
    });
  };
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

          <AppText className="text-xl">{datetime}</AppText>
          {weather}
          <AntDesign
            className="mx-1"
            name="smileo"
            size={20}
            color={Colors.black}
          />
        </View>

        <View className="flex flex-row items-center">
          <TouchableOpacity
            onPress={() =>
              router.push({
                pathname: "/write",
                params: { diaryId, type: "EDIT" },
              })
            }
          >
            <AppText className="text-xl">수정</AppText>
          </TouchableOpacity>
          <Pressable
            onPress={() => {
              onClickDeleteDiary();
            }}
          >
            <AppText className="text-xl ms-4" style={{ color: Colors.red1 }}>
              삭제
            </AppText>
          </Pressable>
        </View>
      </View>
      {/* 이미지 */}
      <View className={`w-full ${photos ? "h-[30%]" : "h-0"} p-4`}>
        {photos && (
          <Image
            source={{ uri: `data:image/jpeg;base64,${photos}` }}
            className="w-full h-full"
            resizeMode="cover"
          />
        )}
      </View>
      {/* 내용 */}
      <ScrollView className={`${photos ? "h-[65%]" : "h-[95]"}`}>
        <AppText className="px-4 text-xl" style={{ lineHeight: 22 }}>
          {content}
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
