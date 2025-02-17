import React, { useRef } from "react";
import {
  NaverMapView,
  NaverMapMarkerOverlay,
  NaverMapViewRef,
} from "@mj-studio/react-native-naver-map";
import { View, StyleSheet, Text, Alert } from "react-native";
import { SearchResponse } from "@/assets/types/map/mapModels";

interface mapProps {
  myLatitude: number;
  myLongitude: number;
  searchResults: SearchResponse["data"]["documents"];
  onCameraIdle: (currentLatitude: number, currentLongitude: number) => void;
}

export default function MapScreen({
  myLatitude,
  myLongitude,
  searchResults,
  onCameraIdle,
}: mapProps) {
  // NaverMapView의 참조를 생성
  const ref = useRef<NaverMapViewRef>(null);

  return (
    <View style={styles.container}>
      <NaverMapView
        ref={ref} // 지도 참조 연결
        style={styles.map}
        initialCamera={{
          latitude: myLatitude,
          longitude: myLongitude,
          zoom: 18, // 초기 줌 레벨
        }}
        onCameraIdle={(params) => {
          const { latitude, longitude } = params;
          onCameraIdle(latitude, longitude);
        }}
      >
        {/* 검색된 장소 마커 */}
        {searchResults?.map((item) => (
          <NaverMapMarkerOverlay
            key={item.id}
            latitude={parseFloat(item.y)}
            longitude={parseFloat(item.x)}
            caption={{
              text: item.place_name,
              haloColor: "white",
              requestedWidth: 5,
              minZoom: 14,
            }}
            width={30}
            height={40}
            isHideCollidedCaptions={true}
          />
        ))}
        {/* 내 위치 마커 */}
        <NaverMapMarkerOverlay
          key="my-location"
          latitude={myLatitude}
          longitude={myLongitude}
          image={require("../../assets/images/blue-marker.png")}
          width={30}
          height={30}
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
