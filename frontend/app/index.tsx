import { Redirect, router } from "expo-router";
import {
  StyleSheet,
  Dimensions,
  Alert,
  Platform,
  PermissionsAndroid,
} from "react-native";
import "../global.css";
import "../constants/variables.css";
import "../constants/common.css";
import { memberInfo } from "@/assets/apis/members";
import { useEffect, useState, ReactNode } from "react";
import { requestLocationPermission } from "@/assets/utils/map";
import AsyncStorage from "@react-native-async-storage/async-storage";
// 새로운 import 방식
import {
  FirebaseMessagingTypes,
  getMessaging,
  getToken,
  onMessage,
} from "@react-native-firebase/messaging";
import { AppProps, HeadlessCheckProps } from "@/assets/types/type";

const { width, height } = Dimensions.get("window");

// FCM 백그라운드 메시지 핸들러 설정 - 컴포넌트 외부에 정의
getMessaging().setBackgroundMessageHandler(
  async (remoteMessage: FirebaseMessagingTypes.RemoteMessage) => {
    return onMessageReceived(remoteMessage);
  }
);

const onMessageReceived = async (
  message: FirebaseMessagingTypes.RemoteMessage
): Promise<void> => {
  console.log("메시지 받는중", message);
};

function MainApp(): JSX.Element | null {
  useEffect(() => {
    getFcmToken();
    const unsubscribe = subscribe();

    const initializeApp = async (): Promise<void> => {
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

        // 알림 권한 요청
        await requestNotificationPermission();

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

  // 알림 권한 요청 함수
  const requestNotificationPermission = async (): Promise<boolean> => {
    try {
      // Android 13+ 권한 요청
      if (
        Platform.OS === "android" &&
        parseInt(Platform.Version.toString(), 10) >= 33
      ) {
        const granted = await PermissionsAndroid.request(
          PermissionsAndroid.PERMISSIONS.POST_NOTIFICATIONS,
          {
            title: "알림 권한 요청",
            message: "앱이 알림을 사용하려면 권한이 필요합니다.",
            buttonNegative: "취소",
            buttonPositive: "확인",
          }
        );

        if (granted === PermissionsAndroid.RESULTS.GRANTED) {
          console.log("Android 알림 권한 승인됨");
          return true;
        } else {
          console.log("Android 알림 권한 거부됨");
          Alert.alert(
            "알림 권한 필요",
            "앱의 알림 기능을 사용하려면 설정에서 알림 권한을 허용해주세요."
          );
          return false;
        }
      }

      return true; // 이전 안드로이드 버전에서는 매니페스트에 권한 선언만으로 충분
    } catch (error) {
      console.error("알림 권한 요청 중 오류 발생:", error);
      return false;
    }
  };

  /** get: FCM 토큰 */
  const getFcmToken = async (): Promise<void> => {
    // 새로운 API 방식
    const messagingInstance = getMessaging();
    const fcmToken = await getToken(messagingInstance);
    console.log("[+] FCM Token :: ", fcmToken);
  };

  /** foreground 메시지 수신 */
  const subscribe = (): (() => void) => {
    // 새로운 API 방식
    const messagingInstance = getMessaging();
    return onMessage(
      messagingInstance,
      async (remoteMessage: FirebaseMessagingTypes.RemoteMessage) => {
        console.log("[+] Remote Message ", JSON.stringify(remoteMessage));
        // 포그라운드 메시지 처리 - 인앱 알림으로 표시할 수 있음
        if (remoteMessage.notification) {
          Alert.alert(
            remoteMessage.notification.title || "새 알림",
            remoteMessage.notification.body || "새로운 알림이 도착했습니다."
          );
        } else {
          Alert.alert("새 알림", JSON.stringify(remoteMessage));
        }
      }
    );
  };

  return null; // 또는 실제 UI를 반환
}

/** 백그라운드 여부 확인 */
function HeadlessCheck({
  isHeadless,
  children,
}: HeadlessCheckProps): JSX.Element | null {
  if (isHeadless) {
    return null;
  }
  return <>{children}</>;
}

export default function App({ isHeadless }: AppProps): JSX.Element {
  return (
    <HeadlessCheck isHeadless={isHeadless}>
      <MainApp />
    </HeadlessCheck>
  );
}
