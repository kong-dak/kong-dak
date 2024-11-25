import { Link, router } from "expo-router";
import { View, Text, StyleSheet, Pressable } from "react-native";
import "../global.css";

export default function HomeScreen() {
  return (
    <View>
      <Text className="text-lg text-red-400 bg-sky-300 pt-8">
        app/index 페이지asd
      </Text>
      <Link href={"/diary"}>app/diary/index로 이동하기</Link>
      <Pressable onPress={() => router.push("/diary/details")}>
        <Text>app/diary/details로 이동하기</Text>
      </Pressable>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
  },
});
