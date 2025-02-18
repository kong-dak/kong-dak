import axios from "axios";

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
    (config) => {
      // /api로 시작하는 요청에 대해 baseURL 변경
      if (config.url?.startsWith("/api/")) {
        config.baseURL = "http://3.26.5.65:8080/";
      }

      // localStorage나 다른 저장소에서 토큰을 가져옴
      //  const token = localStorage.getItem('accessToken');
      const token = "";
      // 토큰이 있으면 헤더에 추가
      if (token) {
        config.headers.Authorization = `Bearer ${token}`;
      } else {
        console.log("토큰이 없습니다.");
      }

      return config;
    },
    (error) => {
      return Promise.reject(error);
    }
  );

  return instance;
}
