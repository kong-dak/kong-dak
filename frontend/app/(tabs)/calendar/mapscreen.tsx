import { NaverMapView } from "@mj-studio/react-native-naver-map";
import React from "react";
import { View, StyleSheet } from "react-native";

export default function MapScreen() {
  return (
    <View style={styles.container}>
      <NaverMapView
        style={styles.map}
        />
    </View>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1 },
  map: { flex: 1 },
});