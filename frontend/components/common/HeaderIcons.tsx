import { Colors } from "@/constants/Colors";
import { AntDesign, Feather } from "@expo/vector-icons";
import { router } from "expo-router";
import { Pressable, View } from "react-native";

type HeaderProps = {
  isBack?: boolean;
};

export default function HeaderIcons({ isBack = false }: HeaderProps) {
  return (
    <View className="w-full flex flex-row justify-between items-center">
      {isBack ? (
        <Pressable
          onPress={() => {
            router.back();
          }}
        >
          <Feather
            className="m-2"
            name="arrow-left"
            size={24}
            color={Colors.main}
          />
        </Pressable>
      ) : (
        <View></View>
      )}
      {isBack ? (
        <View></View>
      ) : (
        <View className="flex flex-row items-center">
          <Feather className="m-2" name="bell" size={24} color={Colors.main} />
          <Pressable
            onPress={() => {
              router.push("/set");
            }}
          >
            <AntDesign
              className="m-2"
              name="setting"
              size={24}
              color={Colors.main}
            />
          </Pressable>
        </View>
      )}
    </View>
  );
}
