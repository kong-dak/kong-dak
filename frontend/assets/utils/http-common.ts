import axios from 'axios';
import { ANDROID_API_URL } from '@env';

//api axios 환경
export default function localAxios() {
    //axios instance 생성
    const instance = axios.create({
        baseURL: ANDROID_API_URL,
        headers: {
            'Content-Type': 'application/json; charset=utf-8',
            accept: 'application/json',
        },
        //쿠키 보내주도록 설정
        withCredentials: true,
    });

    return instance;
}