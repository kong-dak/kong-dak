import { View, Text } from "react-native";
import WheelPicker from "./WheelPicker";
import { useRef } from "react";

interface Time {
  ampm: string;
  hour: string;
  minute: string;
}

interface Props {
  onTimeChange: (time: Time) => void;
  itemHeight: number;
  initValue?: Time;
}

const TimePicker = ({ onTimeChange, itemHeight, initValue }: Props) => {
  const ampmItems = ["AM", "PM"];
  const hourItems = Array.from({ length: 13 }, (_, i) =>
    i.toString().padStart(2, "0")
  );
  const minuteItems = Array.from({ length: 60 }, (_, i) =>
    i.toString().padStart(2, "0")
  );
  const { ampm, hour, minute } = initValue || {};

  const selectedAMPM = useRef("");
  const selectedHour = useRef("");
  const selectedMinute = useRef("");

  const handleIndexChange = (category: string, item: string) => {
    switch (category) {
      case "ampm":
        selectedAMPM.current = item;
        break;
      case "hour":
        selectedHour.current = item;
        break;
      case "minute":
        selectedMinute.current = item;
        break;
      default:
        throw new Error("Invalid time category");
    }

    onTimeChange({
      ampm: selectedAMPM.current,
      hour: selectedHour.current,
      minute: selectedMinute.current,
    });
  };

  return (
    <View
      style={{
        flexDirection: "row",
        height: itemHeight * 3,
        justifyContent: "center",
      }}
    >
      <WheelPicker
        items={ampmItems}
        onItemChange={(item) => handleIndexChange("ampm", item)}
        itemHeight={itemHeight}
        initValue={ampm}
        containerStyle={{ marginRight: 70 }}
      />
      <WheelPicker
        items={hourItems}
        onItemChange={(item) => handleIndexChange("hour", item)}
        itemHeight={itemHeight}
        initValue={hour}
        containerStyle={{ paddingHorizontal: 16 }}
      />
      <View
        style={{
          height: itemHeight * 3,
          alignItems: "center",
          justifyContent: "center",
        }}
      >
        <Text>:</Text>
      </View>
      <WheelPicker
        items={minuteItems}
        onItemChange={(item) => handleIndexChange("minute", item)}
        itemHeight={itemHeight}
        initValue={minute}
        containerStyle={{ paddingHorizontal: 16 }}
      />
      <View
        style={{
          position: "absolute",
          height: itemHeight,
          top: itemHeight,
          left: 0,
          right: 0,
          zIndex: -1,
        }}
      ></View>
    </View>
  );
};

export default TimePicker;
