package com.travelplanner.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RouteResponse {
    private int distance;
    private int duration;
    private List<List<Double>> vertexes; // [ [x, y], [x, y], ... ]
}

