import { useRouter } from "expo-router";
import { View, Text, Button } from "react-native";

export default function CalendarScreen() {
  const router = useRouter();
  return (
    <View>
      <Text>Calendar화면입니다</Text>
      <Button
        title="Go to Map Screen"
        onPress={() => router.push("/calendar/mapscreen")}
      />
    </View>
  );
}