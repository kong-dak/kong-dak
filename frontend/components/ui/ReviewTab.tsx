import React, { useEffect, useState } from "react";
import {
  View,
  Text,
  FlatList,
  ActivityIndicator,
  TouchableOpacity,
} from "react-native";
import { getReviewsByPlace } from "@/assets/apis/maps";
import {
  PlaceReviewResponse,
  PagedReviewResponse,
} from "@/assets/types/map/mapModels";

export default function ReviewTab({ placeId }: { placeId: number }) {
  const [reviews, setReviews] = useState<PlaceReviewResponse[]>([]);
  const [page, setPage] = useState(0);
  const [loading, setLoading] = useState(false);
  const [hasMore, setHasMore] = useState(true);

  const loadReviews = async () => {
    if (!hasMore || loading) return;

    setLoading(true);
    try {
      const response = await getReviewsByPlace(placeId, page);
      const pagedData: PagedReviewResponse = response.data.data;

      const newReviews = pagedData.content;
      setReviews((prev) => [...prev, ...newReviews]);
      setHasMore(pagedData.hasNext);
      setPage((prev) => prev + 1);
    } catch (e) {
      console.error("리뷰 불러오기 실패", e);
    }
    setLoading(false);
  };

  useEffect(() => {
    loadReviews();
  }, []);

  const renderReviewItem = ({ item }: { item: PlaceReviewResponse }) => (
    <View
      style={{
        paddingVertical: 12,
        paddingHorizontal: 16,
        borderBottomWidth: 1,
        borderColor: "#eee",
      }}
    >
      <Text style={{ fontSize: 16, marginBottom: 4 }}>{item.comment}</Text>
      <Text style={{ fontSize: 14, color: "gray" }}>
        ⭐ {item.rating}점 - {item.nickname}
      </Text>
    </View>
  );

  const renderFooter = () => {
    if (loading) return <ActivityIndicator style={{ marginVertical: 16 }} />;

    if (hasMore) {
      return (
        <TouchableOpacity
          style={{
            marginVertical: 16,
            paddingVertical: 12,
            alignItems: "center",
            backgroundColor: "#f0f0f0",
            marginHorizontal: 16,
            borderRadius: 8,
          }}
          onPress={loadReviews}
        >
          <Text style={{ fontSize: 16, fontWeight: "bold" }}>리뷰 더보기</Text>
        </TouchableOpacity>
      );
    }

    return null;
  };

  return (
    <FlatList
      data={reviews}
      keyExtractor={(_, index) => index.toString()}
      renderItem={renderReviewItem}
      ListFooterComponent={renderFooter}
    />
  );
}
