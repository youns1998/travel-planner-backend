package com.travelplanner.backend.service;

import com.travelplanner.backend.domain.User;
import com.travelplanner.backend.dto.OriginDto;
import com.travelplanner.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void updateOrigin(User user, OriginDto dto) {
        user.setOriginAddress(dto.getAddress());
        user.setOriginX(dto.getX());
        user.setOriginY(dto.getY());
        userRepository.save(user);
    }

    public OriginDto getOrigin(User user) {
        OriginDto dto = new OriginDto();
        dto.setAddress(user.getOriginAddress());
        dto.setX(user.getOriginX());
        dto.setY(user.getOriginY());
        return dto;
    }
}
