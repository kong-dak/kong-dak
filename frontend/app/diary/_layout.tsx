import { Stack } from "expo-router";

export default function HomeLayout() {
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
          title: "Home Screen", // index.tsx의 기본 이름을 변경
          headerShown: false,
        }}
      />
      <Stack.Screen
        name="details"
        options={{
          title: "Details Screen", // details.tsx의 기본 이름을 변경
          headerShown: false,
        }}
      />
    </Stack>
  );
}
