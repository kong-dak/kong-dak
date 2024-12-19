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
          title: "code Screen", // index.tsx의 기본 이름을 변경
          headerShown: false,
        }}
      />
      <Stack.Screen
        name="inputcode"
        options={{
          title: "inputcode Screen", // index.tsx의 기본 이름을 변경
          headerShown: false,
        }}
      />
    </Stack>
  );
}
