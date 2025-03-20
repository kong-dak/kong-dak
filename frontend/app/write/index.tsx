import {
  editDiary,
  getDiaryDetail,
  postDiary,
  uploadPhotos,
} from "@/assets/apis/diary";
import { DiaryType } from "@/assets/types/diary/diaryModels";
import { AppButton } from "@/components/common/AppButton";
import { AppText } from "@/components/common/AppText";
import CustomCalendarDiary from "@/components/ui/CustomCalendarDiary";
import { ImagePicker } from "@/components/ui/DropDownPicker";
import { Colors } from "@/constants/Colors";
import { AntDesign } from "@expo/vector-icons";
import { router, useLocalSearchParams } from "expo-router";
import { ChangeEvent, useEffect, useState } from "react";
import {
  View,
  StyleSheet,
  TextInput,
  Image,
  Modal,
  Pressable,
  Alert,
  KeyboardAvoidingView,
  Platform,
  ScrollView,
} from "react-native";
import DropDownPicker from "react-native-dropdown-picker";
import { launchImageLibrary } from "react-native-image-picker";

export default function DiaryWriteScreen() {
  const props = useLocalSearchParams();
  const diaryAPIType = props.type;
  const today = new Date();
  const year = today.getFullYear();
  const month = today.getMonth() + 1;
  const day = today.getDate();

  console.log(today, day);
  const [isModalVisible, setIsModalVisible] = useState<boolean>(false);
  const [showImage, setShowImage] = useState<string>(""); //일기에 표현될 이미지
  const [imageFile, setImageFile] = useState<string>(""); //저장된 multiform data(uri)
  const [imageFormData, setImageFormData] = useState<string>(""); //s3 저장 후 반환받은 데이터
  const [selectedDay, setSelectedDay] = useState<string>(
    year +
      "-" +
      month.toString().padStart(2, "0") +
      "-" +
      day.toString().padStart(2, "0")
  );
  const [selectedDayText, setSelectedDayText] = useState<string>(
    year +
      "년 " +
      month.toString().padStart(2, "0") +
      "월 " +
      day.toString().padStart(2, "0") +
      "일"
  );
  const [diaryDetail, setDiaryDetail] = useState<DiaryType>({
    content: "",
    emotion: "HAPPY",
    weather: "SUNNY",
    diaryDate: selectedDay,
    photoUrls: [imageFormData],
    decorations: [],
  });

  useEffect(() => {
    setDiaryDetail((prev) => ({
      ...prev,
      diaryDate: selectedDay,
    }));
  }, [selectedDay]);

  const sendDiary = async (uri: string) => {
    const cloneDiaryDetail: DiaryType = { ...diaryDetail, photoUrls: [uri] };
    console.log("데이터를 보냅니다.", cloneDiaryDetail);
    await postDiary(cloneDiaryDetail)
      .then((res) => {
        console.log("보냈습니다", res);
        router.push("/(tabs)/diary");
      })
      .catch((error) => {
        console.error("에러 발생:", error);

        // 400 에러 확인
        if (
          error.response &&
          error.response.status === 400 &&
          error.response.data.code === "D002"
        ) {
          Alert.alert(
            "해당 날짜에 다이어리가 존재합니다.",
            "덮여쓰시겠습니까?",
            [{ text: "네" }, { text: "아니요" }]
          );
          // Alert.alert(
          //   "중복 오류",
          //   "선택한 날짜에 이미 다이어리가 존재합니다",
          //   [{ text: "확인" }]
          // );
        }
      });
  };

  const modifyDiary = async (uri: string) => {
    if (uri !== "") {
      const cloneDiaryDetail: DiaryType = { ...diaryDetail, photoUrls: [uri] };
      console.log("보내기전 clonediaryDetail:", cloneDiaryDetail);
      await editDiary(Number(props.diaryId), cloneDiaryDetail).then((res) => {
        console.log("수정했습니다", res.data);
        console.log(res.data.data.changedFields.photos);
        router.push("/(tabs)/diary");
      });
    } else {
      console.log("보내기전 diaryDetail:", diaryDetail);
      await editDiary(Number(props.diaryId), diaryDetail).then((res) => {
        console.log("수정했습니다", res.data);
        console.log(res.data.data.changedFields.photos);
        router.push("/(tabs)/diary");
      });
    }
  };
  const writeDiary = async () => {
    // console.log(diaryDetail);
    if (diaryDetail.content.length > 1000) {
      Alert.alert(
        "알림",
        "1000자를 초과하였습니다. 글자 수를 다시 맞춰주시길 바랍니다.",
        [{ text: "확인" }]
      );
      return;
    }
    if (props.type === "POST") {
      //Formdata 만들기
      if (imageFile.length > 0) {
        makeFormData();
      } else {
        sendDiary("");
      }
    } else if (props.type === "EDIT") {
      console.log("수정할이미지");
      console.log(imageFile);
      if (imageFile.length > 0) {
        makeFormData();
      } else {
        modifyDiary("");
      }
    } else {
      Alert.alert(
        "알림",
        "오류가 발생하였습니다. 다시 시도해주시길 바랍니다.",
        [{ text: "확인" }]
      );
      router.push("/(tabs)/diary");
    }
  };

  const onChangeText = (e: string) => {
    setDiaryDetail((prev) => ({ ...prev, content: e }));
  };

  const onChangeModalBoolean = () => {
    setIsModalVisible((prev) => !prev);
  };

  const settingDate = () => {
    const dateString = selectedDay.split("-");
    const dayText =
      dateString[0] +
      "년 " +
      dateString[1].toString().padStart(2, "0") +
      "월 " +
      dateString[2].toString().padStart(2, "0") +
      "일";
    setSelectedDayText(dayText);
    setIsModalVisible(false);
  };

  const onSelectImage = () => {
    launchImageLibrary(
      {
        mediaType: "photo",
        maxWidth: 1024,
        maxHeight: 512,
        includeBase64: true,
        selectionLimit: 1,
      },
      (res) => {
        // console.log(res);
        // console.log(response.assets[0].base64)
        if (res.didCancel) {
          console.log("이미지 선택을 취소했습니다.");
          return;
        } else if (res.errorCode) {
          console.log("ImagePicker 에러: " + res.errorCode);
        }
        const base64 = res.assets?.[0]?.base64;
        if (base64) {
          setShowImage(base64);
        }

        // uri가 존재하는지 확인
        const fileUri = res.assets?.[0].uri;
        if (fileUri) {
          setImageFile(fileUri);
        }
      }
    );
  };

  const makeFormData = async () => {
    const formData = new FormData();
    const fileNameParts = imageFile.split("/");
    const fileName = fileNameParts[fileNameParts.length - 1];

    // 파일 타입 추정 (확장자에 따라)
    let fileType = "image/jpeg"; // 기본값
    if (fileName.endsWith(".png")) {
      fileType = "image/png";
    } else if (fileName.endsWith(".gif")) {
      fileType = "image/gif";
    }
    // 파일 객체 생성
    const fileObject = {
      uri: imageFile,
      name: fileName,
      type: fileType,
    } as unknown as Blob;

    formData.append("files", fileObject);

    await uploadPhotos(formData).then((res) => {
      console.log(res.data.data);
      setImageFormData(res.data.data.photoUrls[0]);
      setDiaryDetail((prev) => ({
        ...prev,
        photoUrls: [res.data.data.photoUrls[0]],
      }));
      if (props.type === "POST") {
        sendDiary(res.data.data.photoUrls[0]);
      } else {
        modifyDiary(res.data.data.photoUrls[0]);
      }
    });
  };

  useEffect(() => {
    if (diaryAPIType === "EDIT") {
      const initialDiaryProps = async () => {
        await getDiaryDetail(Number(props.diaryId)).then((res) => {
          setDiaryDetail((prev) => ({
            ...prev,
            content: res.data.data.content,
            emotion: res.data.data.emotion,
            weather: res.data.data.weather,
            photoUrls: [res.data.data.photos[0].photoUrl],
            decorations: res.data.data.decorations,
          }));
          setShowImage(res.data.data.photos[0].photoUrl);
        });
      };
      initialDiaryProps();
    }
  }, []);

  return (
    <KeyboardAvoidingView
      style={{ flex: 1 }}
      behavior={Platform.OS === "ios" ? "padding" : "height"}
      keyboardVerticalOffset={Platform.OS === "ios" ? 0 : 0}
    >
      <ScrollView
        contentContainerStyle={{ flexGrow: 1 }}
        keyboardShouldPersistTaps="handled"
        showsVerticalScrollIndicator={false}
      >
        <View style={styles.container}>
          {/* 일기 메인 */}
          <View
            className="flex flex-row justify-between items-center border-b p-2"
            style={{ borderColor: Colors.gray }}
          >
            <Pressable onPress={() => router.back()}>
              <AppText className="text-base">취소</AppText>
            </Pressable>
            <AppText className="text-xl">일기</AppText>
            <Pressable
              onPress={() => {
                writeDiary();
                // router.push("/(tabs)/diary");
              }}
            >
              <AppText className="text-base">작성</AppText>
            </Pressable>
          </View>

          {/* 날씨 기분 날짜 */}
          <View className="flex flex-row justify-between items-center px-2">
            <View className="flex flex-row items-center my-2">
              <View className="flex flex-row items-center me-4">
                <AppText className="text-base me-2">날씨:</AppText>
                <View className="w-24">
                  <ImagePicker type="WEATHER" setState={setDiaryDetail} />
                </View>
              </View>

              <View className="flex flex-row items-center">
                <AppText className="text-base me-2">기분:</AppText>
                <View className="w-24">
                  <ImagePicker type="EMOTION" setState={setDiaryDetail} />
                </View>
              </View>
            </View>

            <Pressable
              className="flex items-center border-b"
              style={{ borderColor: Colors.gray }}
              onPress={() => {
                onChangeModalBoolean();
              }}
            >
              <AppText className="text-base">{selectedDayText}</AppText>
            </Pressable>
          </View>

          <View className="w-full flex-1">
            {/* 일기 작성 */}
            <View
              className={`${
                imageFile ? "flex-[0.7]" : "flex-1"
              } flex justify-between w-[90%]`}
            >
              <TextInput
                className="text-start text-wrap"
                style={[
                  styles.TextInput,
                  {
                    color: Colors.main,
                    outline: "none",
                    flex: 1,
                    flexWrap: "wrap",
                    textAlignVertical: "top",
                  },
                ]}
                placeholder="일기를 작성해주세요."
                placeholderTextColor={Colors.gray}
                value={diaryDetail.content}
                onChangeText={(e) => {
                  onChangeText(e);
                }}
                multiline={true}
              />
              <View className="flex justify-end items-end">
                <AppText
                  className="text-center my-2"
                  style={
                    diaryDetail.content.length < 1000
                      ? { color: Colors.gray }
                      : { color: Colors.red1 }
                  }
                >
                  {diaryDetail.content.length} / 1000자
                </AppText>
              </View>
            </View>

            {/* 이미지 */}
            <View className={`w-full ${showImage ? "flex-[0.3]" : "h-0"} p-4`}>
              {showImage && (
                <Image
                  source={
                    showImage.startsWith("http")
                      ? { uri: showImage } // S3 URL인 경우
                      : { uri: `data:image/jpeg;base64,${showImage}` } // base64 문자열인 경우
                  }
                  className="w-full h-full"
                  resizeMode="contain"
                />
              )}
            </View>
          </View>

          {/* 사진탭 */}
          <Pressable
            onPress={() => onSelectImage()}
            className="relative h-12 flex items-start justify-center ps-2 border-t"
            style={{ borderColor: Colors.black }}
          >
            <AntDesign name="picture" size={26} color="black" />
          </Pressable>

          <Modal
            animationType="fade"
            visible={isModalVisible}
            transparent={true}
          >
            <View
              className="relative h-full w-full flex justify-center items-center"
              style={{ backgroundColor: "rgba(0,0,0,0.2)" }}
            >
              <View className="w-[80%] h-[58%] bg-white rounded-lg p-4">
                <View className="w-full h-[90%]">
                  <CustomCalendarDiary
                    currentDay={selectedDay}
                    setState={setSelectedDay}
                  />
                </View>

                <View className="w-full h-[10%] flex flex-row justify-around items-center">
                  <AppButton
                    text="확인"
                    type="sublight"
                    onPress={() => {
                      settingDate();
                    }}
                  />
                  <AppButton
                    text="취소"
                    type="subbold"
                    outline
                    onPress={() => {
                      setIsModalVisible(false);
                    }}
                  />
                </View>
              </View>
            </View>
          </Modal>
        </View>
      </ScrollView>
    </KeyboardAvoidingView>
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
    padding: 4,
    marginBottom: 2, // 밑줄과의 간격
    outline: "none",
    width: "110%", // 입력창 전체 너비 사용
    outlineColor: "#929292",
    fontSize: 16,
    fontFamily: "GowunDodum-Regular",
  },
});
