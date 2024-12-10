import React from "react";
import { Text, TextProps, TextStyle } from "react-native";

export interface AppTextProps extends TextProps {
  style?: TextStyle;
  children?: React.ReactNode;
}

export function AppText(props: AppTextProps) {
  return (
    <Text
      {...props}
      style={[props.style, { fontFamily: "GowunDodum-Regular" }]}
    >
      {props.children}
    </Text>
  );
}
