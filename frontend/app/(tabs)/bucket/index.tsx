import { BucketListItem, BucketType } from "@/assets/types/type";
import { AppText } from "@/components/common/AppText";
import HeaderIcons from "@/components/common/HeaderIcons";
import { Colors } from "@/constants/Colors";
import { Feather } from "@expo/vector-icons";
import { useCallback, useEffect, useState } from "react";
import { View, StyleSheet, Pressable } from "react-native";
import dummydata from "../../../assets/dummydata/bucketlist.json";
import BucketTheme from "@/components/ui/BucketTheme";
import Checkbox from "expo-checkbox";
import BucketView from "@/components/ui/BucketView";
import { AppButton } from "@/components/common/AppButton";
import BucketInputBox from "@/components/ui/BucketInputBox";
import DragItem from "@/components/ui/DragItem";
import { GestureHandlerRootView } from "react-native-gesture-handler";
import { useFocusEffect } from "@react-navigation/native";
import { getBucket } from "@/assets/apis/bucketlist";

export type RootStackParam = {
  Home: undefined;
};

export default function BucketScreen() {
  const delay = 2000;
  const [bucketType, setBucketType] = useState<BucketType>("ALL");
  const [bucketlist, setBucketList] = useState<BucketListItem[]>(
    dummydata.data
  );
  const [placeList, setPlaceList] = useState<BucketListItem[]>(
    dummydata.data.filter((item) => item.category === "PLACE")
  );
  const [eatList, setEatList] = useState<BucketListItem[]>(
    dummydata.data.filter((item) => item.category === "EAT")
  );
  const [todoList, setTodoList] = useState<BucketListItem[]>(
    dummydata.data.filter((item) => item.category === "TODO")
  );
  const [isCompletedFlag, setIsCompletedFlag] = useState<boolean>(false);
  const [inputBucketText, setInputBucketText] = useState<string>("");
  const [isInputText, setIsInputText] = useState<boolean>(false);

  const mergeData = () => {
    console.log("데이터를 병합합니다.");
    setBucketList([...placeList, ...eatList, ...todoList]);
  };
  const sendData = () => {
    console.log("데이터를 보냅니다. ", bucketlist);
  };

  const getBucketList = async () => {
    await getBucket().then((res) => {
      console.log(res.data);
    });
  };

  useFocusEffect(
    useCallback(() => {
      return () => {
        console.log("떠나기 전에 데이터를 보냅니다.");
        sendData();
      };
    }, [])
  );

  //delay 시간 후 데이터 병합
  useEffect(() => {
    const timer = setTimeout(() => {
      mergeData();
    }, delay);
    return () => clearTimeout(timer);
  }, [placeList, eatList, todoList]);

  useEffect(() => {
    sendData();
  }, bucketlist);

  // useEffect(() => {
  //   getBucketList();
  // },[]);
  const onChangeInputText = (text: string) => {
    setInputBucketText(text); // 상태 업데이트
  };
  const saveInputData = () => {
    if (inputBucketText === "") {
      setIsInputText(false);
    } else {
      if (bucketType === "PLACE") {
        setPlaceList((prev) => [
          ...prev,
          {
            bucketId: placeList.length + 1,
            title: inputBucketText,
            category: bucketType,
            isCompleted: false,
            createdAt: "",
            updatedAt: "",
            order: 0,
          },
        ]);
      } else if (bucketType === "EAT") {
        setEatList((prev) => [
          ...prev,
          {
            bucketId: eatList.length + 1,
            title: inputBucketText,
            category: bucketType,
            isCompleted: false,
            createdAt: "",
            updatedAt: "",
            order: 0,
          },
        ]);
      } else if (bucketType === "TODO") {
        setEatList((prev) => [
          ...prev,
          {
            bucketId: todoList.length + 1,
            title: inputBucketText,
            category: bucketType,
            isCompleted: false,
            createdAt: "",
            updatedAt: "",
            order: 0,
          },
        ]);
      }
      setIsInputText(false);
      setInputBucketText("");
    }
  };
  return (
    <View className="relative items-center w-full" style={styles.container}>
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
            style={{
              color: bucketType === "ALL" ? Colors.white : Colors.main,
            }}
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
      <GestureHandlerRootView style={{ flex: 1, width: "100%" }}>
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
              {bucketType === "ALL" && placeList.length > 0 ? (
                <View className="w-full flex flex-row items-center justify-between">
                  <AppText className="text-3xl my-2">가고 싶은 곳</AppText>
                  <View></View>
                </View>
              ) : null}
              <DragItem
                bucketData={placeList}
                setBucketData={setPlaceList}
                isCompletedFlag={isCompletedFlag}
              />
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
              {bucketType === "ALL" && eatList.length > 0 ? (
                <View className="w-full flex flex-row items-center justify-between">
                  <AppText className="text-3xl my-2">먹고 싶은 것</AppText>
                  <View></View>
                </View>
              ) : null}
              <DragItem
                bucketData={eatList}
                setBucketData={setEatList}
                isCompletedFlag={isCompletedFlag}
              />
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
              {bucketType === "ALL" && todoList.length > 0 ? (
                <View className="w-full flex flex-row items-center justify-between">
                  <AppText className="text-3xl my-2">하고 싶은 일</AppText>
                  <View></View>
                </View>
              ) : null}
              <DragItem
                bucketData={todoList}
                setBucketData={setTodoList}
                isCompletedFlag={isCompletedFlag}
              />
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
      </GestureHandlerRootView>

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
