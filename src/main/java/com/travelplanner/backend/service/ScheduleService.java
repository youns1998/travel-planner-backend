package com.travelplanner.backend.service;

import com.travelplanner.backend.domain.Schedule;
import com.travelplanner.backend.domain.User;
import com.travelplanner.backend.dto.ScheduleDto;
import com.travelplanner.backend.repository.ScheduleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    @Transactional
    public void saveSchedule(User user, List<ScheduleDto> schedules) {
        scheduleRepository.deleteByUser(user);

        List<Schedule> entities = schedules.stream()
                .map(dto -> Schedule.builder()
                        .user(user)
                        .title(dto.getTitle())
                        .mapx(dto.getMapx())
                        .mapy(dto.getMapy())
                        .orderIndex(dto.getOrderIndex())
                        .description(dto.getDescription())
                        .imgUrl(dto.getImgUrl())
                        .address(dto.getAddress()) // ✅ 주소 저장
                        .build())
                .collect(Collectors.toList());

        scheduleRepository.saveAll(entities);
    }

    public List<ScheduleDto> getSchedulesByUser(User user) {
        return scheduleRepository.findByUserOrderByOrderIndex(user).stream()
                .map(entity -> ScheduleDto.builder()
                        .id(entity.getId())
                        .title(entity.getTitle())
                        .mapx(entity.getMapx())
                        .mapy(entity.getMapy())
                        .orderIndex(entity.getOrderIndex())
                        .description(entity.getDescription())
                        .imgUrl(entity.getImgUrl())
                        .address(entity.getAddress()) // ✅ 주소 복원
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public List<ScheduleDto> optimizeSchedule(User user, double originX, double originY) {
        List<Schedule> existing = scheduleRepository.findByUserOrderByOrderIndex(user);
        if (existing.isEmpty()) return List.of();

        List<Schedule> reordered = existing.stream()
                .sorted(Comparator.comparingDouble(s ->
                        Math.pow(s.getMapx() - originX, 2) + Math.pow(s.getMapy() - originY, 2)))
                .collect(Collectors.toList());

        for (int i = 0; i < reordered.size(); i++) {
            reordered.get(i).setOrderIndex(i);
        }

        scheduleRepository.deleteByUser(user);
        scheduleRepository.saveAll(reordered);

        return reordered.stream()
                .map(s -> ScheduleDto.builder()
                        .id(s.getId())
                        .title(s.getTitle())
                        .mapx(s.getMapx())
                        .mapy(s.getMapy())
                        .orderIndex(s.getOrderIndex())
                        .description(s.getDescription())
                        .imgUrl(s.getImgUrl())
                        .address(s.getAddress()) // ✅ 복원에도 포함
                        .build())
                .collect(Collectors.toList());
    }
}
