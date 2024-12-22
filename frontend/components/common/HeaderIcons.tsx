import { Colors } from "@/constants/Colors";
import { AntDesign, Feather } from "@expo/vector-icons";
import { View } from "react-native";

export default function HeaderIcons() {
  return (
    <View className="w-full flex flex-row-reverse">
      <AntDesign className="m-2" name="setting" size={24} color={Colors.main} />
      <Feather className="m-2" name="bell" size={24} color={Colors.main} />
    </View>
  );
}
