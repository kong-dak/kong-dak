import React, { useState } from "react";
import { Modal, View, Text, TouchableOpacity, StyleSheet } from "react-native";
import { PlaceDetailResponse } from "@/assets/types/map/mapModels";
import ReviewCreateModal from "./ReviewCreateModal";
import ReviewTab from "./ReviewTab";
import PlaceHomeTab from "./PlaceHomeTab";

interface PlaceDetailModalProps {
  selectedPlace: PlaceDetailResponse | null;
  closeModal: () => void;
}

export default function PlaceDetailModal({
  selectedPlace,
  closeModal,
}: PlaceDetailModalProps) {
  const [activeTab, setActiveTab] = useState<"home" | "review" | "image">(
    "home"
  );
  const [reviewModalVisible, setReviewModalVisible] = useState(false);

  if (!selectedPlace) return null;

  return (
    <>
      <Modal
        animationType="slide"
        transparent={false}
        visible={!!selectedPlace}
      >
        <View style={styles.modalContainer}>
          {/* 탭 메뉴 */}
          <View style={styles.tabHeader}>
            {(["home", "review", "image"] as const).map((tab) => (
              <TouchableOpacity key={tab} onPress={() => setActiveTab(tab)}>
                <Text
                  style={
                    activeTab === tab ? styles.activeTab : styles.inactiveTab
                  }
                >
                  {tab === "home" ? "홈" : tab === "review" ? "리뷰" : "이미지"}
                </Text>
              </TouchableOpacity>
            ))}
          </View>

          <View style={{ flex: 1 }}>
            {activeTab === "home" && (
              <PlaceHomeTab
                selectedPlace={selectedPlace}
                onWriteReview={() => setReviewModalVisible(true)}
                onShowMoreReviews={() => setActiveTab("review")}
              />
            )}

            {activeTab === "review" && (
              <ReviewTab placeId={selectedPlace.placeId} />
            )}
          </View>

          {/* 닫기 버튼 */}
          <TouchableOpacity style={styles.closeButton} onPress={closeModal}>
            <Text style={styles.closeButtonText}>닫기</Text>
          </TouchableOpacity>
        </View>
      </Modal>

      {/* 리뷰 작성 모달 */}
      <ReviewCreateModal
        visible={reviewModalVisible}
        placeId={selectedPlace.placeId}
        closeModal={() => setReviewModalVisible(false)}
        refreshReviews={() => {}}
      />
    </>
  );
}

const styles = StyleSheet.create({
  modalContainer: {
    flex: 1,
    backgroundColor: "white",
    paddingTop: 50,
  },
  tabHeader: {
    flexDirection: "row",
    justifyContent: "space-around",
    borderBottomWidth: 1,
    borderColor: "#ccc",
  },
  activeTab: {
    paddingVertical: 10,
    fontSize: 16,
    fontWeight: "bold",
    color: "black",
    borderBottomWidth: 2,
    borderColor: "black",
  },
  inactiveTab: {
    paddingVertical: 10,
    fontSize: 16,
    color: "gray",
  },
  closeButton: {
    backgroundColor: "red",
    padding: 15,
    alignItems: "center",
    borderRadius: 10,
    margin: 20,
  },
  closeButtonText: {
    color: "white",
    fontSize: 18,
    fontWeight: "bold",
  },
});
