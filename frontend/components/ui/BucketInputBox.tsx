import {
  TextInput,
  View,
  StyleSheet,
  Text,
  Pressable,
  TouchableWithoutFeedback,
  Keyboard,
  GestureResponderEvent,
} from "react-native";
import { Colors } from "@/constants/Colors";
import { GestureHandlerRootView } from "react-native-gesture-handler";
import { AntDesign } from "@expo/vector-icons";
import { AppText } from "../common/AppText";

interface BucketInputBoxProps {
  value: string;
  onChangeText: (text: string) => void;
  onBlur?: () => void; // 선택적 prop으로 추가
}

export default function BucketInputBox({
  value,
  onChangeText,
  onBlur,
}: BucketInputBoxProps) {
  // AntDesign 버튼 클릭 시 TextInput 내용 초기화
  const handleClear = () => {
    onChangeText(""); // 빈 문자열로 초기화
  };

  return (
    <TouchableWithoutFeedback onPress={Keyboard.dismiss}>
      <View className="w-full flex flex-row items-center justify-between my-1 px-3 border border-gray-300 py-4 rounded-md">
        <GestureHandlerRootView className="w-[90%] flex items-center">
          <TextInput
            className="text-start text-xl "
            placeholder="내용을 입력해주세요"
            placeholderTextColor={Colors.gray}
            value={value}
            onChangeText={onChangeText}
            onBlur={() => {
              onBlur?.(); // optional chaining으로 안전하게 호출
            }}
          />
        </GestureHandlerRootView>

        <View className="flex flex-row items-center justify-center">
          <Pressable
            onPress={() => {
              onBlur?.();
            }}
          >
            <AppText
              className="px-4 py-1 mx-2 rounded-lg flex "
              style={{ color: Colors.white, backgroundColor: Colors.main }}
            >
              등록
            </AppText>
          </Pressable>
          <Pressable onPress={handleClear}>
            <AntDesign name="closecircle" size={24} color={Colors.main} />
          </Pressable>
        </View>
      </View>
    </TouchableWithoutFeedback>
  );
}
