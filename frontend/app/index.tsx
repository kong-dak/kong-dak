import { Redirect } from "expo-router";
import { StyleSheet, Dimensions, Alert } from "react-native";
import "../global.css";
import "../constants/variables.css";
import "../constants/common.css";
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

  return <Redirect href={isLogin ? "/(tabs)" : "/login"} />;
}
