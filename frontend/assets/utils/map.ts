import { PermissionsAndroid, Platform } from "react-native";
import * as Location from "expo-location";

export const requestLocationPermission = async () => {
  if (Platform.OS === "android") {
    try {
      const granted = await PermissionsAndroid.request(
        PermissionsAndroid.PERMISSIONS.ACCESS_FINE_LOCATION,
        {
          title: "위치 권한 요청",
          message: "앱이 위치를 사용하려면 권한이 필요합니다.",
          buttonNegative: "취소",
          buttonPositive: "확인",
        }
      );
      return granted === PermissionsAndroid.RESULTS.GRANTED;
    } catch (err) {
      console.warn(err);
      return false;
    }
  }
  return true; // iOS의 경우 기본적으로 권한이 승인되어 있다고 가정
};

export const fetchLocation = async (): Promise<{
  latitude: number;
  longitude: number;
} | null> => {
  try {
    // 위치 정보 가져오기
    const location = await Location.getCurrentPositionAsync({
      accuracy: Location.Accuracy.High, // 높은 정확도로 설정
    });

    const { latitude, longitude } = location.coords;
    console.log("현재 위치:", latitude, longitude);

    return { latitude, longitude };
  } catch (error) {
    console.error("위치 가져오기 오류:", error);
    return null;
  }
};
