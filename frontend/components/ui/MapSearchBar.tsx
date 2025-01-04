import React, { useState } from "react";
import { View, TextInput, StyleSheet, FlatList, Text, ActivityIndicator } from "react-native";
import { Fontisto } from "@expo/vector-icons";
import { Colors } from "@/constants/Colors";
import { useSearch } from "@/hooks/useSearch";

export default function MapSearchBar() {
  const [query, setQuery] = useState<string>(""); // 검색어 상태
  const { results, loading, handleSearch } = useSearch(); // useSearch 훅에서 로직 가져오기

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
        />
        <Fontisto
          name="zoom"
          size={24}
          color={Colors.main}
          onPress={() => handleSearch(query)} // handleSearch 호출
        />
      </View>

      {/* 로딩 상태 */}
      {loading && <ActivityIndicator size="large" color={Colors.main} />}

      {/* 검색 결과 표시 */}
      <FlatList
        data={results}
        keyExtractor={(item, index) => index.toString()}
        renderItem={({ item }) => (
          <View style={styles.resultItem}>
            <Text style={styles.resultTitle}>{item.title}</Text>
            <Text>{item.address}</Text>
          </View>
        )}
        ListEmptyComponent={loading ? null : <Text>검색 결과가 없습니다.</Text>} // TODO: 비었을 때 별도 컴포넌트로 분리하는게 더 좋음
      />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    marginVertical: 16,
    paddingHorizontal: 8,
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