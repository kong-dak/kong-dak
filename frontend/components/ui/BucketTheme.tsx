import { Colors } from "@/constants/Colors";
import { Feather } from "@expo/vector-icons";
import { GestureResponderEvent, Pressable } from "react-native";

interface bucketprops {
  isSelected: string;
  type: "PLACE" | "TODO" | "EAT";
  icon: "map" | "coffee" | "gift";
  onPress: (event: GestureResponderEvent) => void; // 버튼 클릭 이벤트
}
export default function BucketTheme({
  isSelected,
  type,
  icon,
  onPress,
}: bucketprops) {
  return (
    <Pressable
      className="w-[18%] border-2 rounded-full py-1 mx-2 flex items-center justify-center"
      style={{
        borderColor: Colors.main,
        backgroundColor: isSelected === type ? Colors.main : Colors.white,
      }}
      onPress={onPress}
    >
      <Feather
        className="text-center"
        name={icon}
        size={24}
        color={isSelected === type ? Colors.white : Colors.main}
      />
    </Pressable>
  );
}
