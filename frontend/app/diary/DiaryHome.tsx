import { useNavigation } from "@react-navigation/native";
import { NativeStackNavigationProp } from "@react-navigation/native-stack";
import { View, Text, Button } from "react-native";

export type RootStackParam = {
  Home: undefined;
};

export default function DiaryScreen() {
  const navigation = useNavigation<NativeStackNavigationProp<RootStackParam>>();
  return (
    <View>
      <Text>Diary화면입니다</Text>
      <Button
        title="Go to TestScreen"
        onPress={() => navigation.navigate("Home")}
      />
    </View>
  );
}
