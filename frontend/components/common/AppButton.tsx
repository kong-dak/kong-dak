import React from "react";
import {
  TouchableOpacity,
  Text,
  StyleSheet,
  TextStyle,
  ViewStyle,
  GestureResponderEvent,
} from "react-native";

interface AppButtonProps {
  title: string; // 버튼 텍스트
  onPress: (event: GestureResponderEvent) => void; // 버튼 클릭 이벤트
  style?: ViewStyle; // 버튼의 스타일
  textStyle?: TextStyle; // 버튼 텍스트의 스타일
  disabled?: boolean; // 버튼 비활성화 여부
}

export function AppButton({
  title,
  onPress,
  style,
  textStyle,
  disabled,
}: AppButtonProps) {
  return (
    <TouchableOpacity
      onPress={onPress}
      style={[styles.button, style, disabled && styles.disabled]}
      disabled={disabled}
    >
      <Text style={[styles.text, textStyle]}>{title}</Text>
    </TouchableOpacity>
  );
}

const styles = StyleSheet.create({
  button: {
    backgroundColor: "#b88c6f",
    paddingVertical: 8,
    paddingHorizontal: 24,
    borderRadius: 4,
    alignItems: "center",
    justifyContent: "center",
  },
  text: {
    fontFamily: "GowunDodum-Regular",
    fontSize: 16,
    color: "#FFFFFF",
  },
  disabled: {
    backgroundColor: "#BDBDBD",
  },
});
