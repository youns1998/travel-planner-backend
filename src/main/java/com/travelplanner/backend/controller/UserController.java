package com.travelplanner.backend.controller;

import com.travelplanner.backend.domain.User;
import com.travelplanner.backend.dto.OriginDto;
import com.travelplanner.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public User getMyInfo(@AuthenticationPrincipal User user) {
        return user;
    }

    // ✅ 출발지 좌표 조회
    @GetMapping("/origin")
    public OriginDto getOrigin(@AuthenticationPrincipal User user) {
        return userService.getOrigin(user);
    }

    // ✅ 출발지 좌표 저장
    @PostMapping("/origin")
    public void saveOrigin(@AuthenticationPrincipal User user, @RequestBody OriginDto dto) {
        userService.updateOrigin(user, dto);
    }
}
