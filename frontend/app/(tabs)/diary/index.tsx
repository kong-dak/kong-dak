import { Link } from "expo-router";
import { View, Text, StyleSheet } from "react-native";

export default function DiaryScreen() {
  return (
    <View style={styles.container}>
      <Text>Home</Text>
      <Link href={"/"}>app/index로 이동하기</Link>
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
