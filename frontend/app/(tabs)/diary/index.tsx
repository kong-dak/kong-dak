import { Colors } from "@/constants/Colors";
import { AntDesign } from "@expo/vector-icons";
import { Link } from "expo-router";
import { View, Text, StyleSheet, TextInput } from "react-native";

export default function DiaryScreen() {
  return (
    <View className="items-center" style={styles.container}>
      <View
        className="w-[90%] flex flex-row items-center mt-3 border rounded-full justify-between"
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
