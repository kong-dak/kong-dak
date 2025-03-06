import { memberInfo } from "@/assets/apis/members";
import { AppButton } from "@/components/common/AppButton";
import { AppText } from "@/components/common/AppText";
import { router } from "expo-router";
import { useEffect, useState } from "react";
import { View, StyleSheet } from "react-native";

export default function manageCoupleScreen() {
  const [isConnected, setIsConnected] = useState<boolean>();
  const [coupleId, setCoupleId] = useState<number>();
  const getMyInfo = async () => {
    await memberInfo().then((res) => {
      console.log(res.data.data);
      setIsConnected(res.data.data.coupleInfo.isConnected);
      setCoupleId(res.data.data.coupleId);

      console.log(res.data.data.coupleInfo.isConnected);
    });
  };

  useEffect(() => {
    getMyInfo();
  }, []);
  return (
    <View style={styles.container}>
      <View>
        {/* 커플 매칭이 안됐을때 */}
        <AppButton
          text="커플 매칭하기"
          type="main"
          onPress={() => {
            router.push("/mycode");
          }}
        />
      </View>
      <View>
        {/* 이미 커플일 때 */}
        <AppButton
          text="커플 연결 해제 및 복구"
          type="main"
          onPress={() => {
            router.push({
              pathname: "/set/connectCouple",
              params: {
                coupleId,
                isConnected: isConnected ? 1 : 0,
              },
            });
          }}
        />
      </View>
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
