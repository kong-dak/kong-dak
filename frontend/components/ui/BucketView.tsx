import {
  NativeSyntheticEvent,
  TextInput,
  TextInputChangeEventData,
  View,
  StyleSheet,
} from "react-native";
import { AppText } from "../common/AppText";
import Checkbox from "expo-checkbox";
import { BucketListItem } from "@/assets/types/type";
import { Colors } from "@/constants/Colors";
import { AntDesign, Entypo, Feather, Ionicons } from "@expo/vector-icons";
import { Pressable } from "react-native-gesture-handler";
import { SetStateAction, useEffect, useState } from "react";
import { deleteBucket, editBucket } from "@/assets/apis/bucketlist";

interface BucketViewType {
  id: number;
  title: string;
  category: string;
  isCompleted: boolean;
  bucketData: BucketListItem[];
  onValueChange: (value: boolean) => void;
  setBucketData: React.Dispatch<React.SetStateAction<BucketListItem[]>>;
}
export default function BucketView({
  id,
  title,
  category,
  isCompleted,
  bucketData,
  onValueChange,
  setBucketData,
}: BucketViewType) {
  const [editFlag, setEditFlag] = useState<boolean>(false);
  const [nameEditFlag, setNameEditFlag] = useState<boolean>(false);
  const [editTitle, setEditTitle] = useState<string>(title);

  useEffect(() => {
    setEditFlag(false);
    setNameEditFlag(false);
    setEditTitle(title);
  }, [bucketData]);
  const onChangeEditFlag = async () => {
    setEditFlag(!editFlag);
  };
  const onChangeNameEditFlag = () => {
    setNameEditFlag(!nameEditFlag);
  };

  const deleteBucketDate = async () => {
    const index = bucketData.findIndex((item) => item.bucketId === id);
    if (index !== -1) {
      // filter를 사용해서 해당 id를 제외한 새로운 배열 생성
      const newBucketList = bucketData.filter((item) => item.bucketId !== id);

      await deleteBucket(id).then((res) => {
        console.log("데이터 삭제");
        console.log(res.data);
      });

      setBucketData(newBucketList); // state 업데이트
    }
  };
  const onChangeTitle = (e: NativeSyntheticEvent<TextInputChangeEventData>) => {
    setEditTitle(e.nativeEvent.text);
  };
  const checkInputName = async () => {
    if (editTitle !== "") {
      const index = bucketData.findIndex((item) => item.bucketId === id);
      if (index !== -1) {
        const newBucketList = bucketData.map((item, idx) =>
          idx === index
            ? { ...item, title: editTitle } // 객체를 새로 만들어서 isCompleted 업데이트
            : item
        );
        await editBucket(id, editTitle, bucketData[id].isCompleted).then(
          (res) => {
            console.log("title 데이터 수정");
            console.log(res.data);
          }
        );
        setBucketData(newBucketList);
      }
      setEditFlag(false);
      setNameEditFlag(false);
    } else {
      setEditTitle(title);
      setEditFlag(false);
      setNameEditFlag(false);
    }
  };
  const backInputName = () => {
    setEditFlag(false);
    setNameEditFlag(false);
    setEditTitle(title);
  };
  const truncateString = (str: string, maxLength: number) => {
    if (str.length <= maxLength) return str;
    return str.slice(0, maxLength) + "...";
  };
  const bucketIconComponent = () => {
    if (editFlag && nameEditFlag) {
      return (
        <View className="flex flex-row">
          <Pressable
            onPress={() => {
              backInputName();
            }}
          >
            <AntDesign name="back" size={20} color={Colors.black} />
          </Pressable>
          <Pressable
            onPress={() => {
              checkInputName();
            }}
          >
            <AntDesign
              className="ms-2"
              name="checkcircle"
              size={20}
              color={Colors.main}
            />
          </Pressable>
        </View>
      );
    } else if (editFlag) {
      return (
        <View className="flex flex-row mx-1 justify-center items-center">
          <Pressable
            onPress={() => {
              backInputName();
            }}
          >
            <AntDesign name="back" size={20} color={Colors.black} />
          </Pressable>
          <Pressable
            onPress={() => {
              onChangeNameEditFlag();
            }}
          >
            <Feather
              className="mx-2"
              name="edit-2"
              size={20}
              color={Colors.black}
            />
          </Pressable>

          <Pressable
            onPress={() => {
              deleteBucketDate();
            }}
          >
            <Feather name="trash-2" size={20} color={Colors.red1} />
          </Pressable>
        </View>
      );
    } else {
      return (
        <Checkbox
          value={isCompleted}
          onValueChange={onValueChange}
          color={isCompleted ? Colors.main : "#929292"}
        />
      );
    }
  };
  return (
    <View className="w-full flex flex-row items-center justify-between my-1 px-3">
      <View className="flex flex-row items-center justify-start">
        {category === "PLACE" ? (
          <Ionicons name="airplane-outline" size={24} color={Colors.black} />
        ) : null}
        {category === "EAT" ? (
          <Ionicons name="fast-food-outline" size={24} color={Colors.black} />
        ) : null}
        {category === "TODO" ? (
          <Ionicons name="balloon-outline" size={24} color={Colors.black} />
        ) : null}

        {editFlag && nameEditFlag ? (
          <TextInput
            className="text-start text-2xl h-12 pl-2 bg-red-200 w-[80%]"
            placeholder="내용을 입력해주세요."
            placeholderTextColor={Colors.gray}
            value={editTitle}
            style={styles.TextInput}
            onChange={onChangeTitle}
          />
        ) : (
          <AppText
            className="pl-2 text-2xl"
            numberOfLines={1}
            ellipsizeMode="tail"
          >
            {truncateString(title, 20)}
          </AppText>
        )}
      </View>
      <View className="flex flex-row">
        {bucketIconComponent()}

        {editFlag ? null : (
          <Pressable
            className="flex items-center justify-center"
            onPress={() => {
              onChangeEditFlag();
            }}
          >
            <Entypo
              className="ms-2"
              name="dots-three-horizontal"
              size={18}
              color="black"
            />
          </Pressable>
        )}
      </View>
    </View>
  );
}
const styles = StyleSheet.create({
  TextInput: {
    outline: "none",
    fontFamily: "GowunDodum-Regular",
  },
});
