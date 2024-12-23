import { Stack } from "expo-router";

export default function QuestionLayout() {
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
        name="viewanswer"
        options={{
          title: "Answer Screen", // index.tsx의 기본 이름을 변경
          headerShown: false,
        }}
      />
      <Stack.Screen
        name="writeanswer"
        options={{
          title: "Answer Screen", // index.tsx의 기본 이름을 변경
          headerShown: false,
        }}
      />
    </Stack>
  );
}
