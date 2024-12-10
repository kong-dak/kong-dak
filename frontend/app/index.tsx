import { Redirect } from "expo-router";
import { StyleSheet, Dimensions } from "react-native";
import "../global.css";
import "../constants/variables.css";
import "../constants/common.css";
import { useState } from "react";

const { width, height } = Dimensions.get("window");
const [isLogin, setIsLogin] = useState<boolean>(false);
export default function App() {
  return <Redirect href={isLogin ? "/(tabs)" : "/login"} />;
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    maxWidth: width,
  },
});
