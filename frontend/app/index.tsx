import { Redirect } from "expo-router";
import { StyleSheet, Dimensions } from "react-native";
import "../global.css";
import "../constants/variables.css";
import "../constants/common.css";
import { useState } from "react";
import { memberInfo } from "@/assets/apis/members";

const { width, height } = Dimensions.get("window");

export default function App() {
  const [isLogin, setIsLogin] = useState<boolean>(false);
  const getInfo = async () => {
    await memberInfo().then((res) => {
      const data = res.data.data;
      console.log("로그인 성공");
      console.log("coupleId:", data.coupleInfo.coupleId);
      console.log("memberId:", data.memberId);
      console.log("partnerId:", data.coupleInfo.partnerId);
      console.log("nickname:", data.nickname);
      console.log("createdAt", data.createdAt);
    });
  };
  getInfo();
  return <Redirect href={isLogin ? "/(tabs)" : "/login"} />;
}
