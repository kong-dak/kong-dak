import { AppText } from "@/components/common/AppText";
import { useLocalSearchParams } from "expo-router";
import { View } from "react-native";

export default function KakaoRedirectScreen() {
  const params = useLocalSearchParams();
  const { token } = params;
  return (
    <View>
      <AppText>등록 {token}</AppText>
    </View>
  );
}
