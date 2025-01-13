import { BucketListItem, BucketType } from "@/assets/types/type";
import { AppText } from "@/components/common/AppText";
import HeaderIcons from "@/components/common/HeaderIcons";
import { Colors } from "@/constants/Colors";
import { Feather } from "@expo/vector-icons";
import { useEffect, useState } from "react";
import { View, StyleSheet, Pressable } from "react-native";
import dummydata from "../../../assets/dummydata/bucketlist.json";
import BucketTheme from "@/components/ui/BucketTheme";
import Checkbox from "expo-checkbox";
import BucketView from "@/components/ui/BucketView";
import { AppButton } from "@/components/common/AppButton";
import BucketInputBox from "@/components/ui/BucketInputBox";
export type RootStackParam = {
  Home: undefined;
};

export default function BucketScreen() {
  const [bucketType, setBucketType] = useState<BucketType>("ALL");
  const [bucketlist, setBucketList] = useState<BucketListItem[]>(
    dummydata.data
  );
  const [isCompletedFlag, setIsCompletedFlag] = useState<boolean>(false);
  const [inputBucketText, setInputBucketText] = useState<string>("");
  const [isInputText, setIsInputText] = useState<boolean>(false);

  const changeBucketList = (bucketId: number) => {
    const newBucketList = [...bucketlist];
    newBucketList[bucketId].isCompleted = !newBucketList[bucketId].isCompleted;
    setBucketList(newBucketList);
  };
  const onChangeInputText = (text: string) => {
    setInputBucketText(text); // 상태 업데이트
  };
  const saveInputData = () => {
    if (inputBucketText === "") {
      setIsInputText(false);
    } else {
      setBucketList((prev) => [
        ...prev,
        {
          bucketId: bucketlist.length + 1,
          title: inputBucketText,
          category: bucketType,
          isCompleted: false,
          createdAt: "",
          updatedAt: "",
          order: 0,
        },
      ]);
      setIsInputText(false);
      setInputBucketText("");
    }
  };
  return (
    <View className="relative items-center" style={styles.container}>
      <HeaderIcons />
      <View className="flex flex-row">
        <Pressable
          onPress={() => {
            setBucketType("ALL");
          }}
          className="border-2 rounded-full w-[18%] flex items-center justify-center mx-2"
          style={{
            borderColor: Colors.main,
            backgroundColor: bucketType === "ALL" ? Colors.main : Colors.white,
          }}
        >
          <AppText
            className="text-center text-2xl"
            style={{ color: bucketType === "ALL" ? Colors.white : Colors.main }}
          >
            ALL
          </AppText>
        </Pressable>

        <BucketTheme
          isSelected={bucketType}
          type="PLACE"
          icon="map"
          onPress={() => {
            setBucketType("PLACE");
          }}
        />
        <BucketTheme
          isSelected={bucketType}
          type="EAT"
          icon="coffee"
          onPress={() => {
            setBucketType("EAT");
          }}
        />
        <BucketTheme
          isSelected={bucketType}
          type="TODO"
          icon="gift"
          onPress={() => {
            setBucketType("TODO");
          }}
        />
      </View>
      <View className="w-full flex flex-row items-center justify-between">
        <View></View>
        <View className="flex flex-row p-5 items-center justify-center">
          <AppText className="text-2xl">달성 과제 숨기기</AppText>
          <Checkbox
            className="ml-2"
            value={isCompletedFlag}
            onValueChange={() => {
              setIsCompletedFlag(!isCompletedFlag);
            }}
            color={isCompletedFlag ? Colors.main : "#929292"}
          />
        </View>
      </View>
      <View className="px-2">
        {bucketType === "ALL" || bucketType === "PLACE" ? (
          <View className="mb-4">
            {bucketType === "ALL" ? (
              <View className="w-full flex flex-row items-center justify-between">
                <AppText className="text-3xl my-2">가고 싶은 곳</AppText>
                <View></View>
              </View>
            ) : null}
            {bucketlist.map((item) => {
              if (
                item.category === "PLACE" &&
                !(isCompletedFlag && item.isCompleted)
              ) {
                return (
                  <BucketView
                    title={item.title}
                    category={item.category}
                    isCompleted={item.isCompleted}
                    onValueChange={() => {
                      changeBucketList(item.bucketId - 1);
                    }}
                  />
                );
              }
            })}
          </View>
        ) : null}
        {bucketType === "PLACE" && isInputText ? (
          <BucketInputBox
            value={inputBucketText}
            onChangeText={onChangeInputText}
            onBlur={saveInputData}
          />
        ) : null}
        {bucketType === "ALL" || bucketType === "EAT" ? (
          <View className="mb-4">
            {bucketType === "ALL" ? (
              <View className="w-full flex flex-row items-center justify-between">
                <AppText className="text-3xl my-2">먹고 싶은 것</AppText>
                <View></View>
              </View>
            ) : null}
            {bucketlist.map((item) => {
              if (
                item.category === "EAT" &&
                !(isCompletedFlag && item.isCompleted)
              ) {
                return (
                  <BucketView
                    title={item.title}
                    category={item.category}
                    isCompleted={item.isCompleted}
                    onValueChange={() => {
                      changeBucketList(item.bucketId - 1);
                    }}
                  />
                );
              }
            })}
          </View>
        ) : null}
        {bucketType === "EAT" && isInputText ? (
          <BucketInputBox
            value={inputBucketText}
            onChangeText={onChangeInputText}
            onBlur={saveInputData}
          />
        ) : null}
        {bucketType === "ALL" || bucketType === "TODO" ? (
          <View className="mb-4">
            {bucketType === "ALL" ? (
              <View className="w-full flex flex-row items-center justify-between">
                <AppText className="text-3xl my-2">하고 싶은 일</AppText>
                <View></View>
              </View>
            ) : null}
            {bucketlist.map((item) => {
              if (
                item.category === "TODO" &&
                !(isCompletedFlag && item.isCompleted)
              ) {
                return (
                  <BucketView
                    title={item.title}
                    category={item.category}
                    isCompleted={item.isCompleted}
                    onValueChange={() => {
                      changeBucketList(item.bucketId - 1);
                    }}
                  />
                );
              }
            })}
          </View>
        ) : null}
        {bucketType === "TODO" && isInputText ? (
          <BucketInputBox
            value={inputBucketText}
            onChangeText={onChangeInputText}
            onBlur={saveInputData}
          />
        ) : null}
      </View>
      {bucketType !== "ALL" ? (
        <AppButton
          text="버킷리스트 추가"
          type="main"
          size="big"
          onPress={() => {
            setIsInputText(true);
          }}
          style={{ position: "absolute", bottom: 20 }}
        />
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
