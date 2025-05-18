package com.travelplanner.backend.service;

import com.travelplanner.backend.dto.RouteRequest;
import com.travelplanner.backend.dto.RouteResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class RouteService {

    @Value("${kakao.api.key}")
    private String kakaoApiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public RouteResponse calculateRoute(RouteRequest request) {
        String url = "https://apis-navi.kakaomobility.com/v1/waypoints/directions";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "KakaoAK " + kakaoApiKey);

        Map<String, Object> body = new HashMap<>();
        body.put("origin", request.getOrigin());
        body.put("destination", request.getDestination());
        body.put("waypoints", request.getWaypoints());
        body.put("priority", "RECOMMEND");

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);

        Map<String, Object> responseBody = response.getBody();
        if (responseBody == null || !responseBody.containsKey("routes")) {
            throw new RuntimeException("🚨 경로 계산 실패: 응답에 'routes'가 없습니다.");
        }

        var routes = (List<Map<String, Object>>) responseBody.get("routes");
        if (routes.isEmpty() || !routes.get(0).containsKey("summary")) {
            throw new RuntimeException("🚨 경로 계산 실패: summary가 없습니다.");
        }

        // ✅ 거리, 시간 파싱
        Map<String, Object> summary = (Map<String, Object>) routes.get(0).get("summary");
        Number distNum = (Number) summary.get("distance");
        Number durNum = (Number) summary.get("duration");
        int distance = distNum != null ? distNum.intValue() : 0;
        int duration = durNum != null ? durNum.intValue() : 0;

        // ✅ 선 좌표 (vertexes) 파싱
        List<List<Double>> allVertexes = new ArrayList<>();
        List<Map<String, Object>> sections = (List<Map<String, Object>>) routes.get(0).get("sections");

        if (sections != null) {
            for (Map<String, Object> section : sections) {
                List<Map<String, Object>> roads = (List<Map<String, Object>>) section.get("roads");
                if (roads != null) {
                    for (Map<String, Object> road : roads) {
                        List<Double> flatVertexes = (List<Double>) road.get("vertexes");
                        if (flatVertexes != null && flatVertexes.size() >= 2) {
                            for (int i = 0; i < flatVertexes.size() - 1; i += 2) {
                                allVertexes.add(Arrays.asList(flatVertexes.get(i), flatVertexes.get(i + 1)));
                            }
                        }
                    }
                }
            }
        }

        return new RouteResponse(distance, duration, allVertexes);
    }
}
