import { BucketListItem, BucketType } from "@/assets/types/type";
import { AppText } from "@/components/common/AppText";
import HeaderIcons from "@/components/common/HeaderIcons";
import { Colors } from "@/constants/Colors";
import { Feather } from "@expo/vector-icons";
import { useCallback, useEffect, useRef, useState } from "react";
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
import {
  getBucket,
  registBucket,
  reorderBucket,
} from "@/assets/apis/bucketlist";

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
  const prevListsRef = useRef({
    placeList: [...placeList],
    eatList: [...eatList],
    todoList: [...todoList],
  });
  const mergeData = () => {
    console.log("데이터를 병합합니다.");
    setBucketList([...placeList, ...eatList, ...todoList]);
  };

  const getBucketList = async () => {
    console.log("버킷리스트 get 작동");
    try {
      await getBucket().then((res) => {
        console.log("버킷리스트 확인");
        console.log(res.data);
        setBucketList(res.data.data);
        setPlaceList(
          res.data.data.filter(
            (item: BucketListItem) => item.category === "PLACE"
          )
        );
        setEatList(
          res.data.data.filter(
            (item: BucketListItem) => item.category === "EAT"
          )
        );
        setTodoList(
          res.data.data.filter(
            (item: BucketListItem) => item.category === "TODO"
          )
        );
      });
    } catch {
      console.log("오류");
    }
  };

  const sendBucketList = async (text: string, bucketType: BucketType) => {
    await registBucket(text, bucketType).then((res) => {
      if (res.data.status === 201) {
        const bucketItem: BucketListItem = {
          bucketId: res.data.data.bucketId,
          title: res.data.data.title,
          category: res.data.data.category,
          isCompleted: res.data.data.isCompleted,
          createdAt: res.data.data.createdAt,
          updatedAt: res.data.data.updatedAt,
          orderNum: res.data.data.orderNum,
        };
        if (bucketType === "PLACE") {
          setPlaceList((prev) => [...prev, bucketItem]);
        } else if (bucketType === "EAT") {
          setEatList((prev) => [...prev, bucketItem]);
        } else if (bucketType === "TODO") {
          setTodoList((prev) => [...prev, bucketItem]);
        }
        setIsInputText(false);
        setInputBucketText("");
      }
      console.log("데이터 전송 완료.");
      console.log(res.data);
    });
  };

  // reorderBucket API 호출 함수 추가
  const reorderItem = async (idList: number[]) => {
    try {
      // API 호출 구현 (실제 함수는 적절히 수정해야 합니다)
      console.log(` 카테고리 순서 변경 요청:`, idList);
      await reorderBucket(idList).then((res) => {
        console.log("순서 재정렬");
        console.log(res.data);
      }); // 실제 API 함수 호출
    } catch (error) {
      console.error(`순서 변경 중 오류 발생:`, error);
    }
  };

  const checkListChanges = () => {
    // 세 배열 중 하나라도 변경되었는지 확인
    const placeChanged =
      JSON.stringify(prevListsRef.current.placeList) !==
      JSON.stringify(placeList);
    const eatChanged =
      JSON.stringify(prevListsRef.current.eatList) !== JSON.stringify(eatList);
    const todoChanged =
      JSON.stringify(prevListsRef.current.todoList) !==
      JSON.stringify(todoList);

    // 변경이 감지되면 모든 카테고리의 ID를 하나의 배열로 합쳐서 전송
    if (placeChanged || eatChanged || todoChanged) {
      console.log("버킷 리스트 변경 감지");

      // 모든 카테고리의 ID를 단일 배열로 합치기
      const allIds: number[] = [
        ...placeList.map((item) => item.bucketId),
        ...eatList.map((item) => item.bucketId),
        ...todoList.map((item) => item.bucketId),
      ];

      // API 호출 - 모든 ID를 하나의 배열로 전송
      reorderItem(allIds);

      // 현재 상태 저장
      prevListsRef.current = {
        placeList: [...placeList],
        eatList: [...eatList],
        todoList: [...todoList],
      };
    }
  };

  // 리스트 변경 확인 및 API 호출을 위한 useFocusEffect 추가
  useFocusEffect(
    useCallback(() => {
      // 5초마다 확인하는 인터벌 설정
      const intervalId = setInterval(checkListChanges, 2000);

      // 화면에서 벗어날 때 인터벌 정리
      return () => {
        clearInterval(intervalId);
      };
    }, [placeList, eatList, todoList])
  );

  useFocusEffect(
    useCallback(() => {
      getBucketList();
      return () => {
        checkListChanges();
      };
    }, [])
  );

  //delay 시간 후 데이터 병합
  useEffect(() => {
    console.log("캬캬");
    const timer = setTimeout(() => {
      mergeData();
    }, delay);
    return () => clearTimeout(timer);
  }, [placeList, eatList, todoList]);

  const onChangeInputText = (text: string) => {
    setInputBucketText(text); // 상태 업데이트
  };
  const saveInputData = () => {
    if (inputBucketText === "") {
      setIsInputText(false);
    } else {
      sendBucketList(inputBucketText, bucketType);
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
          className="borderNum-2 rounded-full w-[18%] flex items-center justify-center mx-2"
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
