import { View } from "react-native";
import { AppText } from "../common/AppText";
import Checkbox from "expo-checkbox";
import { BucketListItem } from "@/assets/types/type";
import { Colors } from "@/constants/Colors";
import { Ionicons } from "@expo/vector-icons";

interface BucketViewType {
  title: string;
  category: string;
  isCompleted: boolean;
  onValueChange: (value: boolean) => void;
}
export default function BucketView({
  title,
  category,
  isCompleted,
  onValueChange,
}: BucketViewType) {
  return (
    <View className="w-full flex flex-row items-center justify-between my-1 px-3">
      <View className="flex flex-row items-center justify-center">
        {category === "PLACE" ? (
          <Ionicons name="airplane-outline" size={24} color={Colors.black} />
        ) : null}
        {category === "EAT" ? (
          <Ionicons name="fast-food-outline" size={24} color={Colors.black} />
        ) : null}
        {category === "TODO" ? (
          <Ionicons name="balloon-outline" size={24} color={Colors.black} />
        ) : null}

        <AppText className="pl-2 text-2xl">{title}</AppText>
      </View>
      <View className="flex flex-row">
        <Checkbox
          value={isCompleted}
          onValueChange={onValueChange}
          color={isCompleted ? Colors.main : "#929292"}
        />
      </View>
    </View>
  );
}
