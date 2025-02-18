import { Redirect } from "expo-router";
import { StyleSheet, Dimensions, Alert } from "react-native";
import "../global.css";
import "../constants/variables.css";
import "../constants/common.css";
import { memberInfo } from "@/assets/apis/members";
import { useEffect, useState } from "react";
import { requestLocationPermission } from "@/assets/utils/map";

const { width, height } = Dimensions.get("window");

export default function App() {
  const [isLogin, setIsLogin] = useState<boolean>(false);
  useEffect(() => {
    const checkLocationPermission = async () => {
      const hasLocationPermission = await requestLocationPermission();
      if (!hasLocationPermission) {
        Alert.alert("권한 거부", "위치 서비스를 사용하려면 권한이 필요합니다.");
      } else {
        console.log("위치 권한 승인");
      }
    };

    checkLocationPermission();
  }, []);
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
