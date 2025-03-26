import React from "react";
import { View, Text, TouchableOpacity, StyleSheet, Image } from "react-native";
import { PlaceDetailResponse } from "@/assets/types/map/mapModels";

interface Props {
  selectedPlace: PlaceDetailResponse;
  onWriteReview: () => void;
  onShowMoreReviews: () => void;
}

export default function PlaceHomeTab({
  selectedPlace,
  onWriteReview,
  onShowMoreReviews,
}: Props) {
  return (
    <View>
      <View style={styles.detailsContainer}>
        <Text style={styles.title}>{selectedPlace.placeName}</Text>
        <Text style={styles.category}>{selectedPlace.categoryName}</Text>
        <Text style={styles.address}>📍 {selectedPlace.addressName}</Text>
        <Text style={styles.roadAddress}>
          🚗 {selectedPlace.roadAddressName}
        </Text>
        <Text style={styles.phone}>
          📞 {selectedPlace.phone || "전화번호 없음"}
        </Text>
      </View>

      {/* 리뷰 프리뷰 */}
      <View style={styles.section}>
        <View style={styles.sectionHeader}>
          <Text style={styles.sectionTitle}>리뷰</Text>
          <TouchableOpacity onPress={onWriteReview}>
            <Text style={styles.writeReviewLink}>리뷰 쓰기</Text>
          </TouchableOpacity>
        </View>

        {selectedPlace.previewReviews.length > 0 ? (
          selectedPlace.previewReviews.slice(0, 3).map((review, index) => (
            <View key={index} style={styles.reviewItem}>
              <Text style={styles.reviewText}>{review.comment}</Text>
              <Text style={styles.reviewRating}>
                ⭐ {review.rating}점 - {review.nickname}
              </Text>
            </View>
          ))
        ) : (
          <Text style={styles.placeholderText}>첫 리뷰를 작성해주세요!</Text>
        )}

        {selectedPlace.previewReviews.length > 3 && (
          <TouchableOpacity onPress={onShowMoreReviews}>
            <Text style={styles.moreReviewButton}>리뷰 더보기</Text>
          </TouchableOpacity>
        )}
      </View>

      {/* 운영 시간 */}
      <View style={styles.section}>
        <Text style={styles.sectionTitle}>⏰ 운영 시간</Text>
        {selectedPlace.operatingHours.length > 0 ? (
          selectedPlace.operatingHours.map((op, index) => (
            <Text key={index} style={styles.operatingHourText}>
              {op.dayOfWeek} : {op.openTime} ~ {op.closeTime}
            </Text>
          ))
        ) : (
          <Text style={styles.placeholderText}>운영 시간 정보가 없습니다.</Text>
        )}
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  detailsContainer: { padding: 20 },
  title: { fontSize: 24, fontWeight: "bold" },
  category: { fontSize: 18, color: "gray" },
  address: { fontSize: 16, marginTop: 5 },
  roadAddress: { fontSize: 16, marginTop: 5 },
  phone: { fontSize: 16, marginTop: 5, color: "blue" },
  section: { padding: 10, borderTopWidth: 1, borderColor: "#ccc" },
  sectionHeader: {
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center",
    marginBottom: 5,
  },
  sectionTitle: { fontSize: 20, fontWeight: "bold" },
  writeReviewLink: {
    color: "#007AFF",
    fontSize: 14,
    textDecorationLine: "underline",
  },
  reviewItem: {
    paddingVertical: 10,
    borderBottomWidth: 1,
    borderColor: "#eee",
  },
  reviewText: { fontSize: 16 },
  reviewRating: { fontSize: 14, color: "orange" },
  moreReviewButton: {
    color: "#007AFF",
    textAlign: "center",
    marginTop: 10,
    fontSize: 16,
  },
  placeholderText: {
    fontSize: 16,
    color: "gray",
    textAlign: "center",
    paddingVertical: 10,
  },
  operatingHourText: { fontSize: 16, marginTop: 4 },
});
