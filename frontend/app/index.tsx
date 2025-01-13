import { Redirect } from "expo-router";
import { StyleSheet, Dimensions } from "react-native";
import "../global.css";
import "../constants/variables.css";
import "../constants/common.css";
import { useState } from "react";

const { width, height } = Dimensions.get("window");

export default function App() {
  const [isLogin, setIsLogin] = useState<boolean>(false);
  return <Redirect href={isLogin ? "/(tabs)" : "/login"} />;
}
