import { Colors } from "@/constants/Colors";
import { AntDesign, Feather } from "@expo/vector-icons";
import { router } from "expo-router";
import { TouchableOpacity, View } from "react-native";

type HeaderProps = {
  isBack?: boolean;
};

export default function HeaderIcons({ isBack = false }: HeaderProps) {
  return (
    <View className="w-full flex flex-row justify-between items-center">
      {isBack ? (
        <TouchableOpacity
          onPress={() => {
            router.back();
          }}
        >
          <Feather name="arrow-left" size={24} color={Colors.main} />
        </TouchableOpacity>
      ) : (
        <View></View>
      )}
      <View className="flex flex-row items-center">
        <Feather className="m-2" name="bell" size={24} color={Colors.main} />
        <AntDesign
          className="m-2"
          name="setting"
          size={24}
          color={Colors.main}
        />
      </View>
    </View>
  );
}
