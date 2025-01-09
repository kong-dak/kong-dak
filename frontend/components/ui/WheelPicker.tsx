import React, { useEffect, useRef, useState } from "react";
import {
  Animated,
  ListRenderItemInfo,
  NativeScrollEvent,
  NativeSyntheticEvent,
  Text,
  View,
  ViewStyle,
  StyleSheet,
} from "react-native";

interface Props {
  items: string[];
  onItemChange: (item: string) => void;
  itemHeight: number;
  initValue?: string;
  containerStyle?: ViewStyle;
}

const WheelPicker: React.FC<Props> = (props) => {
  const { items, onItemChange, itemHeight, initValue, containerStyle } = props;
  const scrollY = useRef(new Animated.Value(0)).current;
  const flatListRef = useRef<Animated.FlatList>(null);
  const initValueIndex = initValue ? items.indexOf(initValue) : 0;
  const [selectedIndex, setSelectedIndex] = useState(initValueIndex);

  // 패딩을 위해 위아래로 더미 아이템 추가
  const paddedItems = ["", "", ...items, "", ""];
  const visibleItems = 3; // 한 번에 보이는 아이템 수

  const getItemTransform = (index: number) => {
    const itemOffset = index * itemHeight;
    return scrollY.interpolate({
      inputRange: [
        itemOffset - itemHeight,
        itemOffset,
        itemOffset + itemHeight,
      ],
      outputRange: [0.8, 1, 0.8],
      extrapolate: "clamp",
    });
  };

  const renderItem = ({ item, index }: ListRenderItemInfo<string>) => {
    const actualIndex = index - 2; // 패딩을 고려한 실제 인덱스
    const scale = getItemTransform(index);
    const opacity = getItemTransform(index);

    return (
      <Animated.View
        style={[
          styles.itemContainer,
          {
            height: itemHeight,
            transform: [{ scale }],
            opacity,
          },
        ]}
      >
        <Text
          style={[
            styles.itemText,
            actualIndex === selectedIndex && styles.selectedItemText,
          ]}
        >
          {item}
        </Text>
      </Animated.View>
    );
  };

  const onScroll = Animated.event(
    [{ nativeEvent: { contentOffset: { y: scrollY } } }],
    { useNativeDriver: true }
  );

  const onMomentumScrollEnd = (
    event: NativeSyntheticEvent<NativeScrollEvent>
  ) => {
    const offsetY = event.nativeEvent.contentOffset.y;

    //target 설정
    const newIndex = Math.round(offsetY / itemHeight) - 1; // 패딩 보정
    const clampedIndex = Math.max(0, Math.min(newIndex, items.length - 1));

    setSelectedIndex(clampedIndex);

    // 정확한 위치로 스냅
    const targetOffset = (clampedIndex + 1) * itemHeight;
    flatListRef.current?.scrollToOffset({
      offset: targetOffset,
      animated: true,
    });
  };

  useEffect(() => {
    if (selectedIndex >= 0 && selectedIndex < items.length) {
      onItemChange(items[selectedIndex]);
    }
  }, [selectedIndex]);

  // 초기 스크롤 위치 설정
  useEffect(() => {
    const initialOffset = (initValueIndex + 2) * itemHeight;
    setTimeout(() => {
      flatListRef.current?.scrollToOffset({
        offset: initialOffset,
        animated: false,
      });
    }, 0);
  }, []);

  return (
    <View
      style={[
        styles.container,
        { height: itemHeight * visibleItems },
        containerStyle,
      ]}
    >
      <Animated.FlatList
        ref={flatListRef}
        data={paddedItems}
        renderItem={renderItem}
        showsVerticalScrollIndicator={false}
        snapToInterval={itemHeight}
        decelerationRate="fast"
        onScroll={onScroll}
        onMomentumScrollEnd={onMomentumScrollEnd}
        scrollEventThrottle={16}
        getItemLayout={(_, index) => ({
          length: itemHeight,
          offset: itemHeight * index,
          index,
        })}
        style={styles.flatList}
      />
      <View
        pointerEvents="none"
        style={[styles.highlight, { height: itemHeight }]}
      />
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    overflow: "hidden",
  },
  flatList: {
    flexGrow: 0,
  },
  itemContainer: {
    alignItems: "center",
    justifyContent: "center",
  },
  itemText: {
    fontSize: 16,
    color: "#666",
  },
  selectedItemText: {
    color: "#000",
    fontWeight: "600",
  },
  highlight: {
    position: "absolute",
    top: "33.33%",
    left: 0,
    right: 0,
    borderTopWidth: StyleSheet.hairlineWidth,
    borderBottomWidth: StyleSheet.hairlineWidth,
    borderColor: "#ccc",
  },
});

export default WheelPicker;
