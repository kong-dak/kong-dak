import React from "react";
import { Text, TextProps, TextStyle } from "react-native";

export interface AppTextProps extends TextProps {
  style?: TextStyle;
  children?: React.ReactNode;
  color?: string;
}

export function AppText(props: AppTextProps) {
  return (
    <Text
      {...props}
      style={[
        { fontFamily: "GowunDodum-Regular", color: props.color || "#2c1a1a" },
        props.style,
      ]}
    >
      {props.children}
    </Text>
  );
}
