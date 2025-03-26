import { createReview } from "@/assets/apis/maps";
import { CreateReviewRequest } from "@/assets/types/map/mapModels";
import React, { useState } from "react";
import {
  Modal,
  View,
  Text,
  TextInput,
  TouchableOpacity,
  StyleSheet,
  Alert,
} from "react-native";

// Props 타입 정의
interface ReviewCreateModalProps {
  visible: boolean;
  placeId: number;
  closeModal: () => void;
  refreshReviews: () => void; // ✅ 리뷰 목록을 다시 불러오는 함수 추가
}

export default function ReviewCreateModal({
  visible,
  placeId,
  closeModal,
  refreshReviews,
}: ReviewCreateModalProps) {
  const [rating, setRating] = useState("");
  const [comment, setComment] = useState("");

  const handleSubmit = async () => {
    // ⭐ 유효성 검사 (별점 범위 체크)
    const ratingNum = parseInt(rating);
    if (!rating || ratingNum < 1 || ratingNum > 5) {
      Alert.alert("오류", "별점은 1~5 사이의 숫자로 입력해주세요.");
      return;
    }
    if (!comment.trim()) {
      Alert.alert("오류", "리뷰 내용을 입력해주세요.");
      return;
    }

    try {
      const requestData: CreateReviewRequest = {
        rating: ratingNum,
        comment: comment.trim(),
        imageUrls: [], // ✅ 나중에 이미지 업로드 기능 추가 가능
      };

      await createReview(placeId, requestData);
      Alert.alert("성공", "리뷰가 등록되었습니다!");

      // ✅ 리뷰 목록 갱신
      refreshReviews();

      // ✅ 입력값 초기화 및 모달 닫기
      setRating("");
      setComment("");
      closeModal();
    } catch (error) {
      Alert.alert("오류", "리뷰 등록 중 문제가 발생했습니다.");
      console.error("리뷰 등록 오류:", error);
    }
  };

  return (
    <Modal animationType="slide" transparent={false} visible={visible}>
      <View style={styles.modalContainer}>
        <Text style={styles.title}>리뷰 작성</Text>

        <TextInput
          style={styles.input}
          placeholder="별점 (1~5)"
          keyboardType="numeric"
          value={rating}
          onChangeText={setRating}
        />

        <TextInput
          style={styles.input}
          placeholder="리뷰 내용을 입력하세요"
          value={comment}
          onChangeText={setComment}
        />

        <TouchableOpacity style={styles.submitButton} onPress={handleSubmit}>
          <Text style={styles.submitButtonText}>리뷰 등록</Text>
        </TouchableOpacity>

        <TouchableOpacity style={styles.closeButton} onPress={closeModal}>
          <Text style={styles.closeButtonText}>닫기</Text>
        </TouchableOpacity>
      </View>
    </Modal>
  );
}

const styles = StyleSheet.create({
  modalContainer: { flex: 1, justifyContent: "center", padding: 20 },
  title: { fontSize: 20, fontWeight: "bold", textAlign: "center" },
  input: { borderBottomWidth: 1, padding: 10, marginVertical: 10 },
  submitButton: {
    backgroundColor: "#007AFF",
    padding: 15,
    alignItems: "center",
    borderRadius: 10,
    marginVertical: 10,
  },
  submitButtonText: { color: "white", fontSize: 18 },
  closeButton: {
    backgroundColor: "red",
    padding: 15,
    alignItems: "center",
    borderRadius: 10,
    marginVertical: 10,
  },
  closeButtonText: { color: "white", fontSize: 18 },
});
