import { useState } from "react";
import { search } from "@/assets/apis/maps";

export const useSearch = () => {
  const [results, setResults] = useState<any[]>([]);
  const [loading, setLoading] = useState<boolean>(false);

  const handleSearch = async (query: string) => {
    if (!query) return;
    setLoading(true);
    try {
      const response = await search(query, 10, 1);
      console.log("API 응답 데이터:", response.data);
      setResults(response.data.items);
    } catch (error) {
      console.error("검색 오류:", error);
    } finally {
      setLoading(false);
    }
  };

  return { results, loading, handleSearch };
};