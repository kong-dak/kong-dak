import { useState } from "react";
import DropDownPicker from "react-native-dropdown-picker";
import { StyleSheet } from "react-native";
import { DiaryType } from "@/assets/types/diary/diaryModels";

interface ImagePickerType {
  type: "WEATHER" | "EMOTION";
  setState: React.Dispatch<React.SetStateAction<DiaryType>>;
}

export function ImagePicker(props: ImagePickerType) {
  const [open, setOpen] = useState(false);
  const [value, setValue] = useState(null);
  const item =
    props.type === "WEATHER"
      ? [
          { label: "☀", value: "SUNNY" },
          { label: "☁", value: "CLOUDY" },
          { label: "🌧", value: "RAINY" },
          { label: "🌨", value: "SNOWY" },
        ]
      : [
          { label: "😀", value: "HAPPY" },
          { label: "😂", value: "SAD" },
          { label: "😤", value: "ANGRY" },
          { label: "😆", value: "EXCITED" },
          { label: "😖", value: "NERVOUS" },
        ];
  const [items, setItems] = useState(item);

  const onChangeValue = (e: string | null) => {
    console.log(e);
    if (props.type === "EMOTION") {
      props.setState((prev) => ({ ...prev, emotion: e }));
    } else if (props.type === "WEATHER") {
      props.setState((prev) => ({ ...prev, weather: e }));
    }
  };

  return (
    <DropDownPicker
      open={open}
      value={value}
      items={items}
      setOpen={setOpen}
      setValue={setValue}
      setItems={setItems}
      placeholder={item[0].label}
      listItemContainerStyle={styles.dropdown}
      onChangeValue={(e) => {
        onChangeValue(e);
      }}
      style={{
        borderColor: "gray",
        borderWidth: 1,
        width: "80%",
        minHeight: 10,
      }}
      placeholderStyle={{ padding: 0, margin: 0 }}
      // item 열었을 때 style
      dropDownContainerStyle={{
        borderColor: "gray",
        width: "80%",
        borderTopWidth: 0,
      }}
    />
  );
}

const styles = StyleSheet.create({
  dropdown: {
    backgroundColor: "white",
    padding: 0,
  },
});
