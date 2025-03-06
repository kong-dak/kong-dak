import { AppText } from "@/components/common/AppText";
import { View, StyleSheet } from "react-native";

export default function manageUserScreen() {
  return (
    <View style={styles.container}>
      <AppText>정보</AppText>
    </View>
  );
}
const styles = StyleSheet.create({
  container: {
    backgroundColor: "#fefefe",
    display: "flex",
    padding: 2,
    flex: 1,
  },
});
