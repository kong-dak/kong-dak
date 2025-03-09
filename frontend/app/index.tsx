import { Redirect, router } from "expo-router";
import { StyleSheet, Dimensions, Alert } from "react-native";
import "../global.css";
import "../constants/variables.css";
import "../constants/common.css";
import { memberInfo } from "@/assets/apis/members";
import { useEffect, useState } from "react";
import { requestLocationPermission } from "@/assets/utils/map";
import AsyncStorage from "@react-native-async-storage/async-storage";
// 새로운 import 방식
import {
  getMessaging,
  getToken,
  onMessage,
} from "@react-native-firebase/messaging";

const { width, height } = Dimensions.get("window");

export default function App() {
  useEffect(() => {
    getFcmToken();
    const unsubscribe = subscribe();

    const initializeApp = async () => {
      try {
        // 위치 권한 체크
        const hasLocationPermission = await requestLocationPermission();
        if (!hasLocationPermission) {
          Alert.alert(
            "권한 거부",
            "위치 서비스를 사용하려면 권한이 필요합니다."
          );
        } else {
          console.log("위치 권한 승인");
        }

        // 로그인 상태 체크
        const isLogin = await AsyncStorage.getItem("isLogin");
        if (isLogin === "true") {
          router.navigate("/(tabs)");
        } else {
          // router.navigate("/(tabs)/calendar");
          router.navigate("/login");
        }
      } catch (error) {
        console.error("초기화 중 오류 발생:", error);
        router.navigate("/login"); // 에러 발생 시 로그인 페이지로
      }
    };

    initializeApp();

    return unsubscribe;
  }, []);

  /**
   * FCM 토큰을 받습니다.
   */
  const getFcmToken = async () => {
    // 새로운 API 방식
    const messagingInstance = getMessaging();
    const fcmToken = await getToken(messagingInstance);
    console.log("[+] FCM Token :: ", fcmToken);
  };

  /**
   * FCM 메시지를 앱이 foreground 상태일 경우 메시지를 수신합니다.
   */
  const subscribe = () => {
    // 새로운 API 방식
    const messagingInstance = getMessaging();
    return onMessage(messagingInstance, async (remoteMessage) => {
      console.log("[+] Remote Message ", JSON.stringify(remoteMessage));
    });
  };

  return null; // 또는 로딩 스피너 등을 표시할 수 있습니다
}
