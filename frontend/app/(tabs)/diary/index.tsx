import { AppText } from "@/components/common/AppText";
import HeaderIcons from "@/components/common/HeaderIcons";
import DiaryItem from "@/components/ui/DiaryItem";
import { Colors } from "@/constants/Colors";
import { AntDesign, Feather, MaterialCommunityIcons } from "@expo/vector-icons";
import { Link, router } from "expo-router";
import { useEffect, useState } from "react";
import {
  View,
  Text,
  StyleSheet,
  TextInput,
  TouchableOpacity,
  Pressable,
  ScrollView,
} from "react-native";
import jsonData from "../../../assets/dummydata/diarylist.json";
import { DiaryItemProps } from "@/assets/types/type";
import { getDiaryList } from "@/assets/apis/diary";

export default function getDiaryScreen() {
  const today = new Date();
  const year = today.getFullYear();
  const month = today.getMonth() + 1;
  const [diaryYear, setDiaryYear] = useState<number>(year);
  const [diaryMonth, setDiaryMonth] = useState<number>(month);

  const [diaryItemList, setDiaryItemList] = useState<DiaryItemProps[]>([]);

  const getDiarys = async () => {
    const diaryClone = diaryMonth.toString().padStart(2, "0");
    await getDiaryList(diaryYear + "-" + diaryClone).then((res) => {
      console.log(res.data.data.diaries);
      if (res.data.data.diaries) {
        setDiaryItemList(res.data.data.diaries);
      } else {
        setDiaryItemList([]);
      }
    });
  };

  useEffect(() => {
    getDiarys();
  }, [diaryYear, diaryMonth]);

  useEffect(() => {
    getDiarys();
  }, []);

  const changeMonth = (type: "plus" | "minus") => {
    if (type === "plus") {
      const monthValue = diaryMonth + 1;
      if (monthValue > 12) {
        const yearValue = diaryYear;
        setDiaryYear(yearValue + 1);
        setDiaryMonth(1);
      } else {
        setDiaryMonth(monthValue);
      }
    } else {
      const monthValue = diaryMonth - 1;
      if (monthValue < 1) {
        const yearValue = diaryYear;
        setDiaryYear(yearValue - 1);
        setDiaryMonth(12);
      } else {
        setDiaryMonth(monthValue);
      }
    }
  };
  return (
    <View className="items-center relative" style={styles.container}>
      <HeaderIcons />
      <View
        className="w-[90%] flex flex-row items-center mt-8 border rounded-full justify-between"
        style={{ borderColor: Colors.main }}
      >
        <TextInput
          className="w-[80%] text-start ms-4"
          style={[styles.TextInput, { color: Colors.black }]}
          placeholder="찾을 단어를 입력해주세요"
          placeholderTextColor={Colors.gray}
        />
        <AntDesign
          className="m-2"
          name="setting"
          size={24}
          color={Colors.main}
        />
      </View>

      <View className="w-full flex flex-row items-center mt-8 justify-between py-1">
        <AntDesign
          className="m-2"
          name="arrowleft"
          size={24}
          color={Colors.black}
          onPress={() => {
            changeMonth("minus");
          }}
        />
        <AppText className="text-lg">
          {diaryYear}/{diaryMonth}
        </AppText>

        <AntDesign
          className="m-2"
          name="arrowright"
          size={24}
          color={Colors.black}
          onPress={() => {
            changeMonth("plus");
          }}
        />
      </View>

      <ScrollView
        className="flex-1 w-full"
        showsVerticalScrollIndicator={false}
        contentContainerStyle={{ flexGrow: 1, paddingHorizontal: 8 }}
      >
        <View className="w-full flex flex-row items-center justify-center flex-wrap mt-8">
          {diaryItemList.map((item, index) => {
            return (
              <View key={index} className="w-full m-4">
                <DiaryItem
                  diaryId={item.diaryId}
                  diaryDate={item.diaryDate}
                  content={item.content}
                  weather={item.weather}
                  thumbnailUrl={item.thumbnailUrl}
                />
              </View>
            );
          })}
          <View className="w-[34%] m-4"></View>
        </View>
      </ScrollView>

      <TouchableOpacity
        className=" absolute right-4 bottom-4 p-3 rounded-full flex items-center justify-center"
        style={{ backgroundColor: Colors.main }}
        onPress={() =>
          router.push({
            pathname: "/write",
            params: { year: diaryYear, month: diaryMonth, type: "POST" },
          })
        }
      >
        <MaterialCommunityIcons
          name="pencil-plus-outline"
          size={32}
          color={Colors.white}
        />
      </TouchableOpacity>
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
    padding: 2,
    outlineColor: Colors.main,
    outline: "none",
    fontSize: 16,
    fontFamily: "GowunDodum-Regular",
  },
});
