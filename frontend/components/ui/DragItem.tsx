import {
  TouchableOpacity,
  View,
  Text,
  Pressable,
  StyleSheet,
} from "react-native";
import DraggableFlatList, {
  RenderItemParams,
  ScaleDecorator,
} from "react-native-draggable-flatlist";
import { useCallback, useMemo } from "react";
import { BucketListItem } from "@/assets/types/type";
import BucketView from "./BucketView";
import { editBucket } from "@/assets/apis/bucketlist";

interface DragItemProps {
  bucketData: BucketListItem[];
  setBucketData: React.Dispatch<React.SetStateAction<BucketListItem[]>>;
  isCompletedFlag: boolean;
}

export default function DragItem({
  bucketData,
  setBucketData,
  isCompletedFlag,
}: DragItemProps) {
  // bucketData에서 isCompletedFlag 조건에 맞는 항목만 필터링하여 메모이제이션
  const filteredData = useMemo(() => {
    if (isCompletedFlag === true) {
      return bucketData.filter((item) => !item.isCompleted);
    }
    return bucketData;
  }, [bucketData, isCompletedFlag]);

  // 상태 업데이트 함수 최적화
  const changeBucketList = useCallback(
    (targetItem: BucketListItem) => {
      setBucketData((prevData) =>
        prevData.map((item) =>
          item.bucketId === targetItem.bucketId
            ? { ...item, isCompleted: !item.isCompleted }
            : item
        )
      );
      editBucket(
        targetItem.bucketId,
        targetItem.title,
        targetItem.isCompleted
      ).then((res) => {
        console.log("completed 수정 완료");
        console.log(res.data);
      });
    },
    [] // 의존성 제거 - 함수형 업데이트로 인해 bucketData 의존성이 불필요
  );

  // 드래그 종료 핸들러 메모이제이션
  const handleDragEnd = useCallback(
    ({ data }: { data: BucketListItem[] }) => {
      setBucketData(data);
    },
    [] // 함수형 업데이트로 의존성 제거
  );

  // 렌더 아이템 함수 메모이제이션
  const renderItem = useCallback(
    ({ item, drag, isActive }: RenderItemParams<BucketListItem>) => {
      // 완료된 항목 필터링은 이제 filteredData에서 처리하므로 이 조건은 불필요
      return (
        <ScaleDecorator>
          <View style={styles.itemContainer}>
            <TouchableOpacity
              activeOpacity={1}
              onLongPress={drag}
              disabled={isActive}
              style={styles.dragHandle}
              delayLongPress={50} // 기본값은 500ms입니다. 더 빠른 반응을 위해 값을 줄입니다.
            >
              <Text style={styles.dragIcon}>=</Text>
            </TouchableOpacity>
            <Pressable style={styles.contentContainer}>
              <BucketView
                id={item.bucketId}
                title={item.title}
                category={item.category}
                isCompleted={item.isCompleted}
                bucketData={bucketData}
                onValueChange={() => {
                  changeBucketList(item); // item을 전달
                }}
                setBucketData={setBucketData}
              />
            </Pressable>
          </View>
        </ScaleDecorator>
      );
    },
    [bucketData, changeBucketList, setBucketData]
  );

  return (
    <View>
      <DraggableFlatList
        data={filteredData}
        renderItem={renderItem}
        keyExtractor={(item) => item.bucketId + ""}
        onDragEnd={handleDragEnd}
        contentContainerStyle={styles.flatListContent}
        removeClippedSubviews={true} // 화면 밖 항목 메모리에서 제거
        maxToRenderPerBatch={10} // 한번에 렌더링할 최대 항목 수
        updateCellsBatchingPeriod={50} // 배치 업데이트 주기
        windowSize={10} // 렌더 윈도우 크기
      />
    </View>
  );
}

const styles = StyleSheet.create({
  headerText: {
    fontSize: 24,
    fontWeight: "bold",
    padding: 16,
    backgroundColor: "#ffffff",
    borderBottomWidth: 1,
    borderBottomColor: "#e0e0e0",
  },
  flatListContent: {
    padding: 2,
  },
  itemContainer: {
    flexDirection: "row",
    alignItems: "center",
    backgroundColor: "#ffffff",
    marginVertical: 4,
    padding: 4,
    borderRadius: 4,
    elevation: 2,
    shadowColor: "#000",
    shadowOffset: {
      width: 0,
      height: 1,
    },
    shadowOpacity: 0.2,
    shadowRadius: 1.41,
  },
  dragHandle: {
    marginRight: 4,
    marginLeft: 8,
    padding: 6,
  },
  dragIcon: {
    fontSize: 24,
    color: "#666666",
  },
  contentContainer: {
    flex: 1,
  },
});
