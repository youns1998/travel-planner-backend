package com.travelplanner.backend.controller;

import com.travelplanner.backend.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/places")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;

    // 관광지 키워드 검색 API
    // 예: /api/places/search?keyword=서울
    @GetMapping("/search")
    public String searchPlaces(@RequestParam("keyword") String keyword) {
        return placeService.searchPlaces(keyword);
    }
}
