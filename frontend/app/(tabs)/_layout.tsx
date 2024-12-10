import { Tabs } from "expo-router";
import React from "react";

export default function TabLayout() {
  return (
    <Tabs
      screenOptions={{
        tabBarStyle: {
          position: "absolute", // 항상 위에 떠있도록
          bottom: 0, // 화면 하단에 위치
          height: 60, // 탭바 높이
          // 기타 스타일 설정 가능
        },
        // 필요한 경우 다른 옵션들도 설정 가능
        tabBarShowLabel: true, // 라벨 표시 여부
        tabBarActiveTintColor: "#000", // 활성 탭 색상
        tabBarInactiveTintColor: "#999", // 비활성 탭 색상
      }}
    >
      <Tabs.Screen
        name="index"
        options={{
          title: "홈",
        }}
      />
      <Tabs.Screen name="diary" options={{ title: "일기" }} />
      <Tabs.Screen name="calendar" options={{ title: "일정" }} />
      <Tabs.Screen name="play" options={{ title: "놀이방" }} />
      <Tabs.Screen name="bucket" options={{ title: "버킷" }} />
    </Tabs>
  );
}
