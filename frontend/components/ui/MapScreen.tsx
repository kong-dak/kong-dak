import React, { useState, useEffect, useRef } from "react";
import {
  NaverMapView,
  NaverMapMarkerOverlay,
  NaverMapViewRef,
} from "@mj-studio/react-native-naver-map";
import { View, StyleSheet, Text, Alert } from "react-native";
import { requestLocationPermission } from "@/assets/utils/locationPermission";
import Geolocation from "react-native-geolocation-service";
import { SearchResponse } from "@/assets/types/map/mapModels";

interface searchResultProps {
  searchResults: SearchResponse["data"]["documents"];
}

export default function MapScreen({ searchResults }: searchResultProps) {
  const [hasPermission, setHasPermission] = useState<boolean>(true); // 권한 상태
  const [myLocation, setMyLocation] = useState({
    latitude: 37.5665, // 기본값 (서울)
    longitude: 126.978,
  });

  // NaverMapView의 참조를 생성
  const ref = useRef<NaverMapViewRef>(null);

  return (
    <View style={styles.container}>
      <NaverMapView
        ref={ref} // 지도 참조 연결
        style={styles.map}
        onCameraChanged={(args) =>
          console.log(`Camera Changed: ${JSON.stringify(args)}`)
        }
        onTapMap={(args) =>
          console.log(`Map Tapped at: ${JSON.stringify(args)}`)
        }
      >
        {/* 검색된 장소 마커 */}
        {searchResults?.map((item) => (
          <NaverMapMarkerOverlay
            key={item.id}
            latitude={parseFloat(item.y)}
            longitude={parseFloat(item.x)}
            caption={{ text: item.place_name }}
          />
        ))}
        {/* 내 위치 마커 */}
        <NaverMapMarkerOverlay
          key="my-location"
          latitude={myLocation.latitude}
          longitude={myLocation.longitude}
          caption={{ text: "내 위치" }}
        />
      </NaverMapView>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    height: "45%",
    marginTop: 16,
    borderWidth: 1,
    borderColor: "#C4C4C4",
    shadowColor: "#000",
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 3,
  },
  map: {
    flex: 1,
  },
});
