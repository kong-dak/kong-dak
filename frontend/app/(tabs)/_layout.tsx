import { Colors } from "@/constants/Colors";
import {
  Entypo,
  Feather,
  FontAwesome,
  MaterialCommunityIcons,
} from "@expo/vector-icons";
import { Tabs } from "expo-router";
import React from "react";

export default function TabLayout() {
  return (
    <Tabs
      screenOptions={{
        headerShown: false,
        tabBarShowLabel: false, // 라벨 표시 여부
        tabBarStyle: {
          position: "absolute", // 항상 위에 떠있도록
          bottom: 0, // 화면 하단에 위치
          height: 60, // 탭바 높이
        },
        // 필요한 경우 다른 옵션들도 설정 가능
        tabBarItemStyle: {
          display: "flex",
          justifyContent: "center", // 아이콘 수직 중앙 정렬
          alignItems: "center",
        },
      }}
    >
      <Tabs.Screen
        name="index"
        options={{
          tabBarIcon: ({ focused }) => (
            <FontAwesome
              name="home"
              size={24}
              color={focused ? Colors.main : Colors.gray}
            />
          ),
        }}
      />
      <Tabs.Screen
        name="diary"
        options={{
          tabBarIcon: ({ focused }) => (
            <Feather
              name="book-open"
              size={24}
              color={focused ? Colors.main : Colors.gray}
            />
          ),
        }}
      />
      <Tabs.Screen
        name="calendar"
        options={{
          tabBarIcon: ({ focused }) => (
            <Entypo
              name="calendar"
              size={24}
              color={focused ? Colors.main : Colors.gray}
            />
          ),
        }}
      />
      <Tabs.Screen
        name="play"
        options={{
          tabBarIcon: ({ focused }) => (
            <MaterialCommunityIcons
              name="cards-playing-outline"
              size={24}
              color={focused ? Colors.main : Colors.gray}
            />
          ),
        }}
      />
      <Tabs.Screen
        name="bucket"
        options={{
          tabBarIcon: ({ focused }) => (
            <Feather
              name="feather"
              size={24}
              color={focused ? Colors.main : Colors.gray}
            />
          ),
        }}
      />
    </Tabs>
  );
}
