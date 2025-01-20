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
import { gestureHandlerRootHOC } from "react-native-gesture-handler";
import { useCallback, useEffect, useState } from "react";
import { BucketListItem, BucketType } from "@/assets/types/type";
import BucketView from "./BucketView";

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
  const changeBucketList = useCallback(
    (targetItem: BucketListItem) => {
      const index = bucketData.findIndex(
        (item) => item.bucketId === targetItem.bucketId
      );
      if (index !== -1) {
        const newBucketList = bucketData.map((item, idx) =>
          idx === index ? { ...item, isCompleted: !item.isCompleted } : item
        );
        setBucketData(newBucketList);
      }
    },
    [bucketData]
  );
  const renderItem = ({
    item,
    drag,
    isActive,
  }: RenderItemParams<BucketListItem>) => {
    return isCompletedFlag === true && item.isCompleted === true ? null : (
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
  };

  return (
    <View>
      <DraggableFlatList
        data={bucketData}
        renderItem={renderItem}
        keyExtractor={(item) => item.bucketId + ""}
        onDragEnd={({ data }) => {
          setBucketData(data);
        }}
        contentContainerStyle={styles.flatListContent}
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
