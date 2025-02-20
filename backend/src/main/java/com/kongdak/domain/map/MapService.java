package com.kongdak.domain.map;

import com.kongdak.controller.dto.response.KakaoLocalSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MapService {

    @Value("${kakao.api.rest-api-key}")
    private String restApiKey;

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://dapi.kakao.com")
            .build();

    public KakaoLocalSearchResponse search(String query, String x, String y, String size, String sort) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v2/local/search/keyword.json")
                        .queryParam("query", query)
                        .queryParamIfPresent("x", Optional.ofNullable(x))
                        .queryParamIfPresent("y", Optional.ofNullable(y))
                        .queryParamIfPresent("size", Optional.ofNullable(size))
                        .queryParamIfPresent("sort", Optional.ofNullable(sort))
                        .build())
                .header("Authorization", "KakaoAK " + restApiKey)
                .retrieve()
                .bodyToMono(KakaoLocalSearchResponse.class)
                .block();
    }
}
