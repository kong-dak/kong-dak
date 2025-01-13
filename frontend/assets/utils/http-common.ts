import axios from "axios";

//api axios 환경
export default function localAxios() {
  //axios instance 생성
  const instance = axios.create({
    baseURL: "http://10.0.2.2.:8080",
    headers: {
      "Content-Type": "application/json; charset=utf-8",
      accept: "application/json",
    },
    //쿠키 보내주도록 설정
    withCredentials: true,
  });

  return instance;
}
