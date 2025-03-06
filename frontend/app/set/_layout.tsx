import { Stack } from "expo-router";

export default function SettingLayout() {
  return (
    <Stack
      screenOptions={{
        headerStyle: {
          backgroundColor: "#f4511e",
        },
        headerTintColor: "#fff",
        headerTitleStyle: {
          fontWeight: "bold",
        },
      }}
    >
      <Stack.Screen
        name="index"
        options={{
          title: "Setting Screen", // index.tsx의 기본 이름을 변경
          headerShown: false,
        }}
      />
      <Stack.Screen
        name="manageUser"
        options={{
          title: "Setting Screen", // index.tsx의 기본 이름을 변경
          headerShown: false,
        }}
      />
      <Stack.Screen
        name="changeNick"
        options={{
          title: "Setting Screen", // index.tsx의 기본 이름을 변경
          headerShown: false,
        }}
      />
      <Stack.Screen
        name="withdrawUser"
        options={{
          title: "Setting Screen", // index.tsx의 기본 이름을 변경
          headerShown: false,
        }}
      />
      <Stack.Screen
        name="manageCouple"
        options={{
          title: "Setting Screen", // index.tsx의 기본 이름을 변경
          headerShown: false,
        }}
      />
      <Stack.Screen
        name="connectCouple"
        options={{
          title: "Setting Screen", // index.tsx의 기본 이름을 변경
          headerShown: false,
        }}
      />
    </Stack>
  );
}
