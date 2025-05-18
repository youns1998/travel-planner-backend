package com.travelplanner.backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class RouteRequest {
    private Point origin;
    private Point destination;
    private List<Point> waypoints;

    @Data
    public static class Point {
        private String x; // 경도
        private String y; // 위도
    }
}
