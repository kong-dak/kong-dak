import { Link, router } from "expo-router";
import { View, Text, StyleSheet, Pressable } from "react-native";
import "../global.css";
import { NavigationContainer } from "@react-navigation/native";
import { createNativeStackNavigator } from "@react-navigation/native-stack";
import HomeScreen from "./main/MainHome";
import { createBottomTabNavigator } from "@react-navigation/bottom-tabs";
import DiaryScreen from "./diary/DiaryHome";
import CalendarScreen from "./calendar/CalendarHome";
import PlayScreen from "./play/PlayHome";
import BucketScreen from "./bucket/BucketHome";
import LoginHome from "./login/LoginHome"; // LoginHome 컴포넌트를 가져옴
import LoginScreen from "./login/LoginHome";

const Stack = createNativeStackNavigator();
const Tab = createBottomTabNavigator();
export default function App() {
  return (
    <Tab.Navigator initialRouteName="LoginHome">
      <Tab.Screen
        name="LoginHome"
        component={LoginScreen}
        options={{ headerShown: false }}
      />
      <Tab.Screen
        name="MainHome"
        component={HomeScreen}
        options={{ headerShown: false }}
      />
      <Tab.Screen
        name="DiaryHome"
        component={DiaryScreen}
        options={{ headerShown: false }}
      />
      <Tab.Screen
        name="CalenderHome"
        component={CalendarScreen}
        options={{ headerShown: false }}
      />
      <Tab.Screen
        name="PlayHome"
        component={PlayScreen}
        options={{ headerShown: false }}
      />
      <Tab.Screen
        name="BucketHome"
        component={BucketScreen}
        options={{ headerShown: false }}
      />
    </Tab.Navigator>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
  },
});
