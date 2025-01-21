import React, { Dispatch, SetStateAction, useEffect, useState } from "react";
import { View, TextInput, StyleSheet, TouchableOpacity } from "react-native";
import { Fontisto } from "@expo/vector-icons";
import { Colors } from "@/constants/Colors";

interface MapSearchProps {
  onSearch: (query: string) => void;
}
export default function MapSearchBar({ onSearch }: MapSearchProps) {
  const [query, setQuery] = useState<string>(""); // 검색어 상태

  const handlePressSearch = () => {
    if (!query.trim()) {
      alert("검색어를 입력해주세요."); // 검색어가 비어 있을 때 처리
      return;
    }
    onSearch(query);
  };

  return (
    <View style={styles.container}>
      {/* 검색 입력 */}
      <View style={styles.searchContainer}>
        <TextInput
          style={styles.input}
          placeholder="목적지를 입력해주세요. (선택)"
          placeholderTextColor={Colors.gray}
          value={query}
          onChangeText={setQuery}
          onSubmitEditing={handlePressSearch}
        />
        <TouchableOpacity onPress={handlePressSearch}>
          <Fontisto name="zoom" size={24} color={Colors.main} />
        </TouchableOpacity>
      </View>

      {/* 검색 결과 표시
      <FlatList
        data={results}
        keyExtractor={(item) => item.id} // Document의 id를 키로 사용
        renderItem={({ item }) => (
          <View style={styles.resultItem}>
            <Text style={styles.resultTitle}>{item.place_name}</Text>
            <Text>{item.road_address_name}</Text>
          </View>
        )}
      /> */}
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    marginVertical: 16,
  },
  searchContainer: {
    flexDirection: "row",
    alignItems: "center",
    borderWidth: 1,
    borderColor: Colors.main,
    borderRadius: 8,
    paddingHorizontal: 8,
    marginBottom: 16,
  },
  input: {
    flex: 1,
    fontSize: 16,
    paddingVertical: 8,
    color: Colors.black,
  },
  resultItem: {
    padding: 8,
    borderBottomWidth: 1,
    borderBottomColor: "#ccc",
  },
  resultTitle: {
    fontWeight: "bold",
    fontSize: 16,
  },
});
