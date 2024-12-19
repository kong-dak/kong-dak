import {
  Image,
  StyleSheet,
  Platform,
  View,
  Text,
  Button,
  TouchableOpacity,
} from "react-native";

import { HelloWave } from "@/components/HelloWave";
import ParallaxScrollView from "@/components/ParallaxScrollView";
import { ThemedText } from "@/components/ThemedText";
import { ThemedView } from "@/components/ThemedView";
import { createBottomTabNavigator } from "@react-navigation/bottom-tabs";
import { router } from "expo-router";
import { AppText } from "@/components/common/AppText";
import { AntDesign, Feather } from "@expo/vector-icons";
import { Colors } from "@/constants/Colors";

export default function HomeScreen() {
  return (
    <View className="section">
      <View className="w-full flex flex-row-reverse">
        <AntDesign
          className="m-2"
          name="setting"
          size={24}
          color={Colors.main}
        />
        <Feather className="m-2" name="bell" size={24} color={Colors.main} />
      </View>
      <View className="w-full flex flex-row-reverse">
        <AppText className="font-size-big my-1 mx-2">멀쩡한 바지</AppText>
      </View>
      <View className="w-full flex flex-row-reverse">
        <TouchableOpacity
          className="flex items-center justify-center my-1 mx-2"
          onPress={() => router.push("/mycode")}
        >
          <AppText color={Colors.gray} className="font-size-big">
            상대를 연결해주세요
          </AppText>
        </TouchableOpacity>
      </View>

      <View className="w-[95%] my-6 bg-gray-100 rounded py-3 px-6 flex flex-col items-center justify-center">
        <AppText className="font-size-regular">Day 1</AppText>
        <AppText className="font-size-big my-2">
          상대와 하고싶은 데이트는 무엇인가요?
        </AppText>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  titleContainer: {
    flexDirection: "row",
    alignItems: "center",
    gap: 8,
  },
  stepContainer: {
    gap: 8,
    marginBottom: 8,
  },
  reactLogo: {
    height: 178,
    width: 290,
    bottom: 0,
    left: 0,
    position: "absolute",
  },
});
