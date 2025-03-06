import { deactivateMember } from "@/assets/apis/members";
import { AppButton } from "@/components/common/AppButton";
import { AppText } from "@/components/common/AppText";
import AsyncStorage from "@react-native-async-storage/async-storage";
import { router } from "expo-router";
import { View, StyleSheet, GestureResponderEvent } from "react-native";

export default function withdrawUserScreen() {
  const onClickDelete = async () => {
    await deactivateMember().then((res) => {
      console.log("삭제되었습니다.");
      AsyncStorage.clear();
      router.navigate("/");
    });
  };

  return (
    <View style={styles.container}>
      <AppButton
        text="탈퇴하기"
        type="main"
        onPress={() => {
          onClickDelete();
        }}
      />
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
