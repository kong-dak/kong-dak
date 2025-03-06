import { Pressable } from "react-native";
import { AppText } from "../common/AppText";
import { Href, router } from "expo-router";

// props 타입을 인터페이스로 정의
interface SettingItemProps {
  path: Href;
  title?: string; // 선택적 제목 속성 추가
}

// 컴포넌트가 객체 형태의 props를 받도록 수정
export default function SettingItem({
  path,
  title = "title",
}: SettingItemProps) {
  return (
    <Pressable
      className="border-y border-y-gray-200 mx-2"
      onPress={() => {
        router.push(path);
      }}
    >
      <AppText className="p-4 text-lg">{title}</AppText>
    </Pressable>
  );
}
