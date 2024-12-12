import { Image, StyleSheet, Platform, View, Text, Button } from "react-native";

import { HelloWave } from "@/components/HelloWave";
import ParallaxScrollView from "@/components/ParallaxScrollView";
import { ThemedText } from "@/components/ThemedText";
import { ThemedView } from "@/components/ThemedView";
import { createBottomTabNavigator } from "@react-navigation/bottom-tabs";
import { router } from "expo-router";
import { AppText } from "@/components/common/AppText";

export default function HomeScreen() {
  return (
    <View className="color-bg-white h-full flex items-center px-2 py-2">
      <View className="flex flex-row justify-end items-center"></View>
      <AppText>Home화면입니다</AppText>
      <Button title="Go to TestScreen" onPress={() => router.push("/diary")} />
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
