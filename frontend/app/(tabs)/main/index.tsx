import { useNavigation } from "@react-navigation/native";
import { NativeStackNavigationProp } from "@react-navigation/native-stack";
import { Button, View, Text } from "react-native";

export type RootStackParam = {
  MainHome: undefined;
  LoginHome: undefined;
};

export default function MainScreen() {
  const navigation = useNavigation<NativeStackNavigationProp<RootStackParam>>();
  return (
    <View>
      <Text>Home화면입니다</Text>
      <Button
        title="Go to TestScreen"
        onPress={() => navigation.navigate("LoginHome")}
      />
    </View>
  );
}
