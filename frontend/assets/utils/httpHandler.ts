import { AxiosError } from "axios";
import { Alert } from "react-native";

export const handleApiError = (error: AxiosError) => {
  const status = error.response?.status;
  console.error("API 에러 발생:", status);

  const errorMessages: { [key: number]: string } = {
    400: "잘못된 요청입니다.",
    401: "인증이 필요합니다.",
    403: "권한이 없습니다.",
    404: "리소스를 찾을 수 없습니다.",
    500: "서버 오류가 발생했습니다.",
    502: "게이트웨이 오류가 발생했습니다.",
    503: "서비스를 사용할 수 없습니다.",
  };

  // 에러 상태 코드에 따른 처리
  if (status) {
    // Alert로 에러 메시지 표시
    Alert.alert(
      "오류",
      errorMessages[status] || "알 수 없는 오류가 발생했습니다."
    );

    // 콘솔에 자세한 에러 정보 기록
    console.error("에러 상세 정보:", {
      status,
      message: error.message,
      data: error.response?.data,
      url: error.config?.url,
    });
  } else {
    // 네트워크 오류 등 status가 없는 경우
    Alert.alert("오류", "네트워크 연결을 확인해주세요.");
  }
};

export default handleApiError;
