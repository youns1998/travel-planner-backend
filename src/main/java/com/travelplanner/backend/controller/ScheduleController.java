package com.travelplanner.backend.controller;

import com.travelplanner.backend.domain.User;
import com.travelplanner.backend.dto.ScheduleDto;
import com.travelplanner.backend.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping("")
    public ResponseEntity<Void> saveSchedule(
            @AuthenticationPrincipal User user,
            @RequestBody List<ScheduleDto> schedules
    ) {
        scheduleService.saveSchedule(user, schedules);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<ScheduleDto>> getMySchedules(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(scheduleService.getSchedulesByUser(user));
    }

    @PostMapping("/optimize")
    public ResponseEntity<List<ScheduleDto>> optimizeSchedule(
            @AuthenticationPrincipal User user,
            @RequestParam double originX,
            @RequestParam double originY
    ) {
        List<ScheduleDto> result = scheduleService.optimizeSchedule(user, originX, originY);
        return ResponseEntity.ok(result);
    }
}
