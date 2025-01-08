import { BarButtonProps } from "@/assets/types/type";
import { Pressable, Image, Text } from "react-native";
import { Colors } from "@/constants/Colors";

export default function LongBarButton({
  color = "white",
  imgSrc,
  text,
  onPress,
  style,
}: BarButtonProps) {
  let bgColor = Colors.white;
  let borderColor = Colors.black;
  if (color === "yellow") {
    bgColor = Colors.yellow1;
    borderColor = Colors.yellow1;
  }
  return (
    <Pressable
      onPress={onPress}
      style={[
        style,
        { backgroundColor: bgColor, borderColor: borderColor, borderWidth: 1 },
      ]}
      className="flex flex-row justify-center items-center py-2 rounded-md"
    >
      {imgSrc ? (
        <Image
          source={imgSrc}
          style={{ width: 16, height: 16, marginRight: 4 }}
        />
      ) : null}
      <Text>{text}</Text>
    </Pressable>
  );
}
