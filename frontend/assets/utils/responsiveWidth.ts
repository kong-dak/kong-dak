import { Dimensions } from "react-native";

/**
 * 디자인 기준 화면 사이즈 (가로 기준)
 */
const baseDesignScreenSize = 375;
const { width } = Dimensions.get("window");

/**
 * 모바일 화면 크기에 맞게 조절된 사이즈를 반환 해주는 함수
 * @param baseDesignElementSize 디자인 기준 개체 사이즈
 * @returns 화면 비율에 맞게 조절된 크기를 반환
 */
export default function responsiveWidth(baseDesignElementSize: number): number {
  const screenRatio = width / baseDesignScreenSize;

  return baseDesignElementSize * screenRatio;
}
