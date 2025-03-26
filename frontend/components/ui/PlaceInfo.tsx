import React, { useRef, useEffect } from "react";
import {
  View,
  Text,
  FlatList,
  Dimensions,
  TouchableOpacity,
} from "react-native";
import { SearchResponse } from "@/assets/types/map/mapModels";

const SCREEN_WIDTH = Dimensions.get("window").width;
const CARD_WIDTH = SCREEN_WIDTH * 0.8; // 카드 크기
const CARD_MARGIN = (SCREEN_WIDTH - CARD_WIDTH) / 10; // 카드 간격 설정

interface PlaceInfoProps {
  searchResults: SearchResponse["data"]["documents"];
  selectedPlace: SearchResponse["data"]["documents"][0] | null;
  setSelectedPlace: (place: SearchResponse["data"]["documents"][0]) => void;
  onPlacePress: (placeId: number) => void;
}

export default function PlaceInfo({
  searchResults,
  selectedPlace,
  setSelectedPlace,
  onPlacePress,
}: PlaceInfoProps) {
  const flatListRef = useRef<FlatList>(null);

  useEffect(() => {
    if (searchResults.length > 0 && flatListRef.current) {
      flatListRef.current.scrollToOffset({ offset: 0, animated: false });
    }
  }, [searchResults]);

  // ✅ selectedIndex 변경 시 해당 카드로 이동
  useEffect(() => {
    if (selectedPlace && flatListRef.current) {
      const index = searchResults.findIndex(
        (item) => item.id === selectedPlace.id
      );
      if (index !== -1) {
        flatListRef.current.scrollToIndex({
          index,
          animated: true,
          viewPosition: 0.5,
        });
      }
    }
  }, [selectedPlace, searchResults]);

  return (
    <View className="w-full h-full items-center">
      <FlatList
        ref={flatListRef}
        data={searchResults}
        horizontal
        pagingEnabled
        keyExtractor={(item) => item.id}
        showsHorizontalScrollIndicator={false}
        snapToAlignment="center"
        decelerationRate="fast"
        initialNumToRender={1}
        renderItem={({ item, index }) => (
          <TouchableOpacity
            onPress={() => onPlacePress(Number(item.id))}
            activeOpacity={0.8}
          >
            <View
              className="bg-white p-4 rounded-2xl shadow-lg justify-center items-center border border-black"
              style={{
                width: CARD_WIDTH,
                // ✅ 첫 번째 & 마지막 카드 마진 조정
                marginLeft: index === 0 ? CARD_MARGIN * 2 : CARD_MARGIN,
                marginRight:
                  index === searchResults.length - 1
                    ? CARD_MARGIN * 2
                    : CARD_MARGIN,
              }}
            >
              <Text className="text-xl font-bold text-black">
                📍 {item.place_name}
              </Text>
              <Text className="text-base text-gray-600 mt-2">
                주소: {item.address_name}
              </Text>
              <Text className="text-base text-gray-600">
                도로명 주소: {item.road_address_name}
              </Text>
              {item.phone && (
                <Text className="text-base text-gray-600">📞 {item.phone}</Text>
              )}
              {item.place_url && (
                <Text className="text-blue-500 underline mt-2">
                  {item.place_url}
                </Text>
              )}
            </View>
          </TouchableOpacity>
        )}
        // ✅ 최종 위치에서 선택된 카드 확인
        onMomentumScrollEnd={(event) => {
          const index = Math.round(
            event.nativeEvent.contentOffset.x / (CARD_WIDTH + CARD_MARGIN)
          );
          setSelectedPlace(searchResults[index]);
        }}
      />
    </View>
  );
}
