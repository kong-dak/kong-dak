import React from "react";
import {
  StyleSheet,
  ViewStyle,
  GestureResponderEvent,
  Pressable,
} from "react-native";
import { AppText } from "./AppText";
import { Colors } from "@/constants/Colors";

interface AppButtonProps {
  text: string; // 버튼 텍스트
  type: string;
  onPress: (event: GestureResponderEvent) => void; // 버튼 클릭 이벤트
  style?: ViewStyle; // 버튼의 스타일
  disabled?: boolean; // 버튼 비활성화 여부
  outline?: boolean;
  size?: "big" | "middle" | "small";
}

export function AppButton({
  text,
  type,
  onPress,
  style,
  disabled,
  outline,
  size = "middle",
}: AppButtonProps) {
  let btnColor;
  let btnOutline;
  let btnText;
  if (type === "subbold") {
    btnColor = Colors.subbold;
    btnOutline = Colors.subbold;
    btnText = Colors.white;
  } else if (type === "sublight") {
    btnColor = Colors.sublight;
    btnOutline = Colors.sublight;
    btnText = Colors.white;
  } else if (type === "red") {
    btnColor = Colors.red1;
    btnOutline = Colors.red1;
    btnText = Colors.white;
  } else {
    btnColor = Colors.main;
    btnOutline = Colors.main;
    btnText = Colors.white;
  }
  if (outline === true) {
    btnColor = btnText;
    btnText = btnOutline;
  }
  let fontSize = 14;
  if (size === "big") {
    fontSize = 18;
  } else if (size === "small") {
    fontSize = 12;
  }
  if (fontSize)
    return (
      <Pressable
        onPress={onPress}
        style={[
          styles.button,
          {
            backgroundColor: btnColor,
            borderWidth: 1,
            borderColor: btnOutline,
          },
          style,
          disabled && styles.disabled,
        ]}
        disabled={disabled}
      >
        <AppText style={{ color: btnText, fontSize }}>{text}</AppText>
      </Pressable>
    );
}

const styles = StyleSheet.create({
  button: {
    paddingVertical: 10,
    paddingHorizontal: 32,
    borderRadius: 4,
    alignItems: "center",
    justifyContent: "center",
  },
  disabled: {
    backgroundColor: "#BDBDBD",
  },
});
