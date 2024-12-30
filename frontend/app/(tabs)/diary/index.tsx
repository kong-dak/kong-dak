import { AppText } from "@/components/common/AppText";
import HeaderIcons from "@/components/common/HeaderIcons";
import DiaryItem from "@/components/ui/DiaryItem";
import { Colors } from "@/constants/Colors";
import { AntDesign, Feather, MaterialCommunityIcons } from "@expo/vector-icons";
import { Link, router } from "expo-router";
import {
  View,
  Text,
  StyleSheet,
  TextInput,
  TouchableOpacity,
} from "react-native";

export default function DiaryScreen() {
  return (
    <View className="items-center relative" style={styles.container}>
      <HeaderIcons />
      <View
        className="w-[90%] flex flex-row items-center mt-8 border rounded-full justify-between"
        style={{ borderColor: Colors.main }}
      >
        <TextInput
          className="w-[80%] text-start ms-4"
          style={[styles.TextInput, { color: Colors.black }]}
          placeholder="찾을 단어를 입력해주세요"
          placeholderTextColor={Colors.gray}
        />
        <AntDesign
          className="m-2"
          name="setting"
          size={24}
          color={Colors.main}
        />
      </View>

      <View
        className="w-full flex flex-row items-center mt-8 justify-between py-1"
        style={{ backgroundColor: Colors.lightgray2 }}
      >
        <AntDesign
          className="m-2"
          name="arrowleft"
          size={24}
          color={Colors.black}
        />
        <AppText className="text-lg">2024/11</AppText>

        <AntDesign
          className="m-2"
          name="arrowright"
          size={24}
          color={Colors.black}
        />
      </View>

      <View className="w-full flex flex-row items-center justify-center flex-wrap mt-8">
        <View className="w-[34%] m-4">
          <DiaryItem />
        </View>
        <View className="w-[34%] m-4">
          <DiaryItem />
        </View>
        <View className="w-[34%] m-4">
          <DiaryItem />
        </View>
        <View className="w-[34%] m-4"></View>
      </View>

      <TouchableOpacity
        className=" absolute right-4 bottom-4 p-3 rounded-full flex items-center justify-center"
        style={{ backgroundColor: Colors.main }}
        onPress={() => router.push("/write")}
      >
        <MaterialCommunityIcons
          name="pencil-plus-outline"
          size={32}
          color={Colors.white}
        />
      </TouchableOpacity>
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
  TextInput: {
    padding: 2,
    outlineColor: Colors.main,
    outline: "none",
    fontSize: 16,
    fontFamily: "GowunDodum-Regular",
  },
});
