import { memberRestore } from "@/assets/apis/members";
import { AppButton } from "@/components/common/AppButton";
import { AppText } from "@/components/common/AppText";
import { router } from "expo-router";
import { View, StyleSheet, Alert } from "react-native";

export default function restoreUserScreen() {
  const restoreUser = async () => {
    await memberRestore().then((res) => {
      console.log(res.data);
      if (res.data.success) {
        router.navigate("/(tabs)");
      } else {
        Alert.alert("알림", "오류가 발생하였습니다", [{ text: "확인" }]);
        router.navigate("/");
      }
    });
  };
  return (
    <View style={styles.container}>
      <AppText>복구페이지</AppText>
      <AppButton type="main" text="복구하기" onPress={restoreUser} />
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
