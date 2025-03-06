import { disconnect, restore } from "@/assets/apis/couples";
import { memberInfo } from "@/assets/apis/members";
import { AppButton } from "@/components/common/AppButton";
import { AppText } from "@/components/common/AppText";
import { router, useLocalSearchParams } from "expo-router";
import { useEffect, useState } from "react";
import { View, StyleSheet } from "react-native";

export default function connectCoupleScreen() {
  const param = useLocalSearchParams();
  const paramConnected = param.isConnected === "1" ? true : false;
  const paramId = param.coupleId;
  const [isConnected, setIsConnected] = useState<boolean>(paramConnected);
  const [coupleId, setCoupleId] = useState<number>(Number(paramId));
  const getMyInfo = async () => {
    await memberInfo().then((res) => {
      console.log(res.data.data);
      setIsConnected(res.data.data.coupleInfo.isConnected);
      setCoupleId(res.data.data.coupleInfo.coupleId);
    });
  };

  const restoreCouple = async () => {
    await restore(coupleId).then((res) => {
      console.log("복구합니다 커플");
      console.log(res.data.data);
      router.navigate("/(tabs)");
    });
  };

  const disconnectCouple = async () => {
    await disconnect(coupleId).then((res) => {
      console.log("해제합니다 커플");
      console.log(res.data.data);
      router.navigate("/(tabs)");
    });
  };

  useEffect(() => {
    if (paramConnected === undefined || paramId === undefined) {
      getMyInfo();
    }
  }, []);
  return (
    <View style={styles.container}>
      {isConnected && coupleId ? (
        <View>
          {/* 커플이 맺어져있을 때 */}
          <AppButton
            text="커플 해제하기"
            type="main"
            onPress={disconnectCouple}
          />
        </View>
      ) : (
        <View>
          {/* 커플을 해제했을 때 */}
          <AppButton text="커플 복구하기" type="main" onPress={restoreCouple} />
        </View>
      )}
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
