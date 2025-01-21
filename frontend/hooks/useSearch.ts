import { Dispatch, SetStateAction, useState } from "react";
import { search } from "@/assets/apis/maps";
import { SearchParams, SearchResponse } from "@/assets/types/map/mapModels";

export const useSearch = (
  results: SearchResponse["data"]["documents"],
  setResults: Dispatch<SetStateAction<SearchResponse["data"]["documents"]>>
) => {
  const handleSearch = async (
    query: string,
    optionalParams?: Omit<SearchParams, "query">
  ) => {
    if (!query.trim()) return;

    try {
      const params: SearchParams = {
        query,
        ...optionalParams,
      };

      const response = await search(params);

      setResults(response.data.data.documents);
    } catch (error) {
      console.error("검색 오류:", error);
    }
  };

  return { results, handleSearch };
};
