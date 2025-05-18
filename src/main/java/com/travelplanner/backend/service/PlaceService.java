package com.travelplanner.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class PlaceService {

    @Value("${visitkorea.api.key}")
    private String serviceKey;

    @Value("${visitkorea.api.url}")
    private String baseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public String searchPlaces(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return "{\"error\": \"❌ keyword 파라미터가 비어 있습니다.\"}";
        }

        String encodedKeyword;
        try {
            encodedKeyword = URLEncoder.encode(keyword, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            return "{\"error\": \"❌ 키워드 인코딩 실패: " + e.getMessage().replace("\"", "'") + "\"}";
        }

        String rawUrl = UriComponentsBuilder
                .fromUriString(baseUrl + "/searchKeyword1")
                .queryParam("serviceKey", serviceKey)
                .queryParam("MobileOS", "ETC")
                .queryParam("MobileApp", "TravelPlanner")
                .queryParam("keyword", encodedKeyword)
                .queryParam("arrange", "A")
                .queryParam("contentTypeId", "12")
                .queryParam("numOfRows", "10")
                .queryParam("pageNo", "1")
                .queryParam("_type", "json")
                .build(false) // 절대 인코딩하지 않음
                .toUriString();

        System.out.println("🔎 최종 요청 URL: " + rawUrl);

        try {
            URI uri = new URI(rawUrl); // 👉 강제로 URI 인스턴스를 만들어 RestTemplate 인코딩 차단

            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "Mozilla/5.0");
            headers.setAccept(MediaType.parseMediaTypes("application/json"));

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    uri, // 여기! URI 인스턴스를 넘겨서 인코딩 무력화
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            return response.getBody();

        } catch (Exception e) {
            return "{\"error\": \"❌ 외부 API 호출 실패: " + e.getMessage().replace("\"", "'") + "\"}";
        }
    }
}
