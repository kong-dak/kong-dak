import React from "react";
import {
  TouchableOpacity,
  StyleSheet,
  ViewStyle,
  GestureResponderEvent,
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
}

export function AppButton({
  text,
  type,
  onPress,
  style,
  disabled,
  outline,
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
  } else {
    btnColor = Colors.main;
    btnOutline = Colors.main;
    btnText = Colors.white;
  }
  if (outline === true) {
    btnColor = btnText;
    btnText = btnOutline;
  }
  return (
    <TouchableOpacity
      onPress={onPress}
      style={[
        styles.button,
        { backgroundColor: btnColor, borderWidth: 1, borderColor: btnOutline },
        style,
        disabled && styles.disabled,
      ]}
      disabled={disabled}
    >
      <AppText style={{ color: btnText }}>{text}</AppText>
    </TouchableOpacity>
  );
}

const styles = StyleSheet.create({
  button: {
    paddingVertical: 12,
    paddingHorizontal: 32,
    borderRadius: 4,
    alignItems: "center",
    justifyContent: "center",
  },
  disabled: {
    backgroundColor: "#BDBDBD",
  },
});
