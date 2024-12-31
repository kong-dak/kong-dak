package com.kongdak.domain.calendar;

import com.kongdak.controller.dto.response.NaverSearchApiResponse;
import com.kongdak.controller.dto.response.NaverSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MapService {

    @Value("${naver.api.client-id}")
    private String clientId;

    @Value("${naver.api.client-secret}")
    private String clientSecret;

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://openapi.naver.com")
            .build();

    public List<NaverSearchResponse> search(String query, int display, int start) {

        NaverSearchApiResponse response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/search/local.json")
                        .queryParam("query", query)
                        .queryParam("display", display)
                        .queryParam("start", start)
                        .build())
                .header("X-Naver-Client-Id", clientId)
                .header("X-Naver-Client-Secret", clientSecret)
                .retrieve()
                .bodyToMono(NaverSearchApiResponse.class)
                .block();

        return response.items();
    }

}
