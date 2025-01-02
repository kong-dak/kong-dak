import { BucketType } from "@/assets/types/type";
import { AppText } from "@/components/common/AppText";
import HeaderIcons from "@/components/common/HeaderIcons";
import { Colors } from "@/constants/Colors";
import { Feather } from "@expo/vector-icons";
import { useState } from "react";
import { View, StyleSheet, Pressable } from "react-native";

export type RootStackParam = {
  Home: undefined;
};

export default function BucketScreen() {
  const [bucketType, setBucketType] = useState<BucketType>("all");
  return (
    <View className=" items-center" style={styles.container}>
      <HeaderIcons />
      <View className="flex flex-row">
        <Pressable
          onPress={() => {
            setBucketType("all");
          }}
          className="border-2 rounded-full w-[18%] flex items-center justify-center mx-2"
          style={{
            borderColor: Colors.main,
            backgroundColor: bucketType === "all" ? Colors.main : Colors.white,
          }}
        >
          <AppText
            className="text-center text-2xl"
            style={{ color: bucketType === "all" ? Colors.white : Colors.main }}
          >
            ALL
          </AppText>
        </Pressable>

        <Pressable
          className="w-[18%] border-2 rounded-full py-1 mx-2 flex items-center justify-center"
          style={{
            borderColor: Colors.main,
            backgroundColor: bucketType === "trip" ? Colors.main : Colors.white,
          }}
          onPress={() => {
            setBucketType("trip");
          }}
        >
          <Feather
            className="text-center"
            name="map"
            size={24}
            color={bucketType === "trip" ? Colors.white : Colors.main}
          />
        </Pressable>

        <Pressable
          className="w-[18%] border-2 rounded-full py-1 mx-2 flex items-center justify-center"
          style={{
            borderColor: Colors.main,
            backgroundColor: bucketType === "food" ? Colors.main : Colors.white,
          }}
          onPress={() => {
            setBucketType("food");
          }}
        >
          <Feather
            className="text-center"
            name="coffee"
            size={24}
            color={bucketType === "food" ? Colors.white : Colors.main}
          />
        </Pressable>
        <Pressable
          className="w-[18%] border-2 rounded-full py-1 mx-2 flex items-center justify-center"
          style={{
            borderColor: Colors.main,
            backgroundColor: bucketType === "do" ? Colors.main : Colors.white,
          }}
          onPress={() => {
            setBucketType("do");
          }}
        >
          <Feather
            className="text-center"
            name="gift"
            size={24}
            color={bucketType === "do" ? Colors.white : Colors.main}
          />
        </Pressable>
      </View>
      <View className="w-full flex flex-row items-center justify-between">
        <View></View>
        <View className="flex flex-row">
          <AppText>달성 과제 숨기기</AppText>
          <AppText>ㅁ</AppText>
        </View>
      </View>
      {bucketType === "all" || bucketType === "trip" ? (
        <View className="mb-4">
          {bucketType === "all" ? (
            <View className="w-full flex flex-row items-center justify-between">
              <AppText className="text-2xl my-2">가고 싶은 곳</AppText>
              <View></View>
            </View>
          ) : null}
          <View className="w-full flex flex-row items-center justify-between">
            <View>
              <AppText className="text-xl">일본 여행가기</AppText>
            </View>
            <View className="flex flex-row">
              <AppText>ㅁ</AppText>
            </View>
          </View>
        </View>
      ) : null}
      {bucketType === "all" || bucketType === "food" ? (
        <View className="mb-4">
          {bucketType === "all" ? (
            <View className="w-full flex flex-row items-center justify-between">
              <AppText className="text-2xl my-2">먹고 싶은 것</AppText>
              <View></View>
            </View>
          ) : null}
          <View className="w-full flex flex-row items-center justify-between">
            <View>
              <AppText className="text-xl">돼지고기</AppText>
            </View>
            <View className="flex flex-row">
              <AppText>ㅁ</AppText>
            </View>
          </View>
        </View>
      ) : null}
      {bucketType === "all" || bucketType === "do" ? (
        <View className="mb-4">
          {bucketType === "all" ? (
            <View className="w-full flex flex-row items-center justify-between">
              <AppText className="text-2xl my-2">하고 싶은 일</AppText>
              <View></View>
            </View>
          ) : null}
          <View className="w-full flex flex-row items-center justify-between">
            <View>
              <AppText className="text-xl">100일 기념 파티</AppText>
            </View>
            <View className="flex flex-row">
              <AppText>ㅁ</AppText>
            </View>
          </View>
        </View>
      ) : null}
    </View>
  );
}
const styles = StyleSheet.create({
  container: {
    backgroundColor: "#fefefe",
    display: "flex",
    padding: 2,
    flex: 1,
  },
});
