package com.travelplanner.backend.controller;

import com.travelplanner.backend.dto.RouteRequest;
import com.travelplanner.backend.dto.RouteResponse;
import com.travelplanner.backend.service.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/route")
@RequiredArgsConstructor
public class RouteController {

    private final RouteService routeService;

    @PostMapping("/calc")
    public RouteResponse calculateRoute(@RequestBody RouteRequest request) {
        return routeService.calculateRoute(request);
    }
}
