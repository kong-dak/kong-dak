import { AppText } from "@/components/common/AppText";
import { useNavigation } from "@react-navigation/native";
import { NativeStackNavigationProp } from "@react-navigation/native-stack";
import { router } from "expo-router";
import { View, Text, StyleSheet, TouchableOpacity } from "react-native";

export default function KakaoScreen() {
  return (
    <View className="section justify-between" style={styles.container}>
      <View className="h-[30%]">
        <View className="h-[70%]" />
      </View>

      <View className="w-full flex items-center h-[20%]">
        <View className="mb-8 w-[80%]">
          <TouchableOpacity
            className=" bg-yellow-300 flex items-center justify-center py-2 rounded-sm my-2"
            onPress={() => router.push("/login/setnick")}
          >
            <Text>동의하고 계속하기</Text>
          </TouchableOpacity>
        </View>
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
  kakao: {
    backgroundColor: "#b88c6f",
    paddingVertical: 8,
    paddingHorizontal: 24,
    borderRadius: 4,
    alignItems: "center",
    justifyContent: "center",
  },
  text: {
    fontFamily: "GowunDodum-Regular",
    fontSize: 16,
    color: "#FFFFFF",
  },
  disabled: {
    backgroundColor: "#BDBDBD",
  },
});
