import axios from "axios";
import handleApiError from "./httpHandler";
import AsyncStorage from "@react-native-async-storage/async-storage";
import { router } from "expo-router";

//api axios 환경
export default function localAxios() {
  //axios instance 생성
  const instance = axios.create({
    baseURL: "http://10.0.2.2:8080/",
    headers: {
      "Content-Type": "application/json; charset=utf-8",
      accept: "application/json",
    },
    //쿠키 보내주도록 설정
    withCredentials: true,
  });

  // 요청 인터셉터 추가
  instance.interceptors.request.use(
    async (config) => {
      // /api로 시작하는 요청에 대해 baseURL 변경
      if (config.url?.startsWith("/api/")) {
        config.baseURL = "http://3.26.5.65:8080/";
      }

      // AsyncStorage 설정
      // await AsyncStorage.setItem(
      //   "accessToken",
      //   ""
      // );
      // await AsyncStorage.setItem(
      //   "refreshToken",
      //   ""
      // );

      // 토큰 가져오기
      const token = await AsyncStorage.getItem("accessToken");

      // 토큰이 있으면 헤더에 추가
      if (token) {
        config.headers.Authorization = `Bearer ${token}`;
      } else {
        console.log("토큰이 없습니다.");
      }

      return config;
    },
    (error) => {
      console.log("[-] 요청 중 에러가 발생되었습니다.. ", error);
      return Promise.reject(error);
    }
  );

  // 응답 인터셉터
  instance.interceptors.response.use(
    (response) => {
      return response;
    },
    async (error) => {
      handleApiError(error);
      const originalRequest = error.config;

      // 토큰이 만료되어 401 에러가 발생했고, 아직 재시도하지 않은 요청인 경우
      if (
        (error.response.status === 401 || error.response.status === 403) &&
        !originalRequest._retry
      ) {
        originalRequest._retry = true;

        try {
          // refreshToken으로 새로운 accessToken 발급 요청
          const refreshToken = await AsyncStorage.getItem("refreshToken");
          const response = await axios.post(
            "http://3.26.5.65:8080/api/auth/refresh",
            { refreshToken }
          );

          const { accessToken: newAccessToken } = response.data;

          // 새로운 accessToken 저장
          await AsyncStorage.setItem("accessToken", newAccessToken);

          // 새로운 토큰으로 헤더 업데이트
          originalRequest.headers.Authorization = `Bearer ${newAccessToken}`;

          // 실패했던 요청 재시도
          return instance(originalRequest);
        } catch (error) {
          // refreshToken으로도 인증에 실패한 경우
          console.log("토큰 갱신 실패:", error);

          // 로그인 페이지로 리다이렉트하거나 에러 처리
          await AsyncStorage.clear();

          router.navigate("/login");
          // 에러를 다시 던져서 호출한 컴포넌트에서 처리할 수 있도록 함
          return Promise.reject(error);
        }
      }

      return Promise.reject(error);
    }
  );

  return instance;
}
