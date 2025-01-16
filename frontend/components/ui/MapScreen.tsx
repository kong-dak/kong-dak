import { NaverMapView } from "@mj-studio/react-native-naver-map";
import React from "react";
import { View, StyleSheet } from "react-native";

export default function MapScreen() {
  return (
    <View style={styles.container}>
      <NaverMapView style={styles.map} />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    height: "45%",
    marginTop: 16,
    borderWidth: 1,
    borderColor: "#C4C4C4",
    shadowColor: "#000", // 그림자 색상
    shadowOffset: { width: 0, height: 4 }, // 그림자 위치
    shadowOpacity: 0.1, // 그림자 투명도
    shadowRadius: 4, // 그림자 퍼짐 정도
    elevation: 3, // 안드로이드 그림자
  },
  map: {
    flex: 1,
  },
});
