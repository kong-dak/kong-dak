import { View, Text } from "react-native";
import WheelPicker from "./WheelPicker";
import { SetStateAction, useRef } from "react";

interface Time {
  ampm: string;
  hour: string;
  minute: string;
}

interface Props {
  onTimeChange: (time: Time) => void;
  itemHeight: number;
  initValue?: Time;
  isTimePickerStart: boolean;
  isTimePickerEnd: boolean;
  setStartTime: React.Dispatch<React.SetStateAction<string>>;
  setEndTime: React.Dispatch<React.SetStateAction<string>>;
  startTime: string;
  endTime: string;
}

const TimePicker = ({
  onTimeChange,
  itemHeight,
  initValue,
  isTimePickerStart,
  isTimePickerEnd,
  startTime,
  endTime,
  setStartTime,
  setEndTime,
}: Props) => {
  const ampmItems = ["오전", "오후"];
  const hourItems = Array.from({ length: 13 }, (_, i) =>
    i.toString().padStart(2, "0")
  );
  const minuteItems = Array.from({ length: 60 }, (_, i) =>
    i.toString().padStart(2, "0")
  );

  const initialStart = {
    ampm: Number(startTime.split(":")[0]) >= 12 ? "오후" : "오전",
    hour:
      Number(startTime.split(":")[0]) > 12
        ? Number(startTime.split(":")[0]) - 12 + ""
        : startTime.split(":")[0],
    minute: startTime.split(":")[1],
  };
  const initialEnd = {
    ampm: Number(endTime.split(":")[0]) >= 12 ? "오후" : "오전",
    hour:
      Number(endTime.split(":")[0]) > 12
        ? Number(endTime.split(":")[0]) - 12 + ""
        : endTime.split(":")[0],
    minute: endTime.split(":")[1],
  };

  const { ampm, hour, minute } =
    isTimePickerStart === true ? initialStart : initialEnd;

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
    if (isTimePickerStart) {
      const timeHour =
        selectedAMPM.current === "오후"
          ? Number(selectedHour.current) + 12
          : Number(selectedHour.current);
      const timeMinute = selectedMinute.current;
      setStartTime(timeHour + ":" + timeMinute);
    } else {
      const timeHour =
        selectedAMPM.current === "오후"
          ? Number(selectedHour.current) + 12
          : Number(selectedHour.current);
      const timeMinute = selectedMinute.current;
      setEndTime(timeHour + ":" + timeMinute);
    }
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
