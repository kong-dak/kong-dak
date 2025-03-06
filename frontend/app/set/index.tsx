import { getHistory } from "@/assets/apis/daily-questions";
import { QuestionListItem } from "@/assets/types/question/questionModels";
import { AppText } from "@/components/common/AppText";
import HeaderIcons from "@/components/common/HeaderIcons";
import SettingItem from "@/components/ui/SettingItem";
import { Colors } from "@/constants/Colors";
import { router } from "expo-router";
import { useEffect, useState } from "react";
import {
  View,
  StyleSheet,
  ScrollView,
  TouchableOpacity,
  Pressable,
} from "react-native";

export default function SettingScreen() {
  return (
    <View style={styles.container}>
      <HeaderIcons isBack />
      <SettingItem path={"/set/manageUser"} title="회원 정보 관리" />
      <SettingItem path={"/set/manageCouple"} title="커플 관리" />
      <SettingItem path={"/set/changeNick"} title="별명 변경하기" />
      <SettingItem path={"/set/withdrawUser"} title="탈퇴하기" />

      <ScrollView className="text-3xl py-4 px-6"></ScrollView>
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
