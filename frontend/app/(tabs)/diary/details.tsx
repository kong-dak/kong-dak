import { Link } from "expo-router";
import { View, Text, StyleSheet } from "react-native";

export default function DetailsScreen() {
  return (
    <View style={styles.container}>
      <Text className="text-lg text-red-400 bg-sky-300 pt-8">Details</Text>
      <Link href={"/diary"}>app/diary/index로 이동하기</Link>
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
