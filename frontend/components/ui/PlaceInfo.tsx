import React from "react";
import { View, Text, StyleSheet } from "react-native";
import { SearchResponse } from "@/assets/types/map/mapModels";

interface PlaceInfoProps {
  selectedMarker: SearchResponse["data"]["documents"][0] | null;
}

export default function PlaceInfo({ selectedMarker }: PlaceInfoProps) {
  if (!selectedMarker) return null; // 선택된 마커가 없으면 렌더링하지 않음

  return (
    <View style={styles.container}>
      <Text style={styles.title}>📍 {selectedMarker.place_name}</Text>
      <Text style={styles.detail}>주소: {selectedMarker.address_name}</Text>
      <Text style={styles.detail}>
        도로명 주소: {selectedMarker.road_address_name}
      </Text>
      {selectedMarker.phone && (
        <Text style={styles.detail}>전화번호: {selectedMarker.phone}</Text>
      )}
      {selectedMarker.place_url && (
        <Text style={styles.detail}>홈페이지: {selectedMarker.place_url}</Text>
      )}
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    backgroundColor: "#fff",
    padding: 10,
    borderTopWidth: 1,
    borderColor: "#ccc",
    alignItems: "center",
  },
  title: {
    fontSize: 16,
    fontWeight: "bold",
  },
  detail: {
    fontSize: 14,
    color: "#666",
  },
});
