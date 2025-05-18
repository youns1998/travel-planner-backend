package com.travelplanner.backend.dto;

import com.travelplanner.backend.domain.Schedule;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleDto {

    private Long id;
    private String title;
    private double mapx;
    private double mapy;
    private int orderIndex;
    private String description; // 설명
    private String imgUrl; // 이미지 URL
    private String address; // ✅ 추가된 주소 필드

    public static ScheduleDto fromEntity(Schedule schedule) {
        return ScheduleDto.builder()
                .id(schedule.getId())
                .title(schedule.getTitle())
                .mapx(schedule.getMapx())
                .mapy(schedule.getMapy())
                .orderIndex(schedule.getOrderIndex())
                .description(schedule.getDescription())
                .imgUrl(schedule.getImgUrl())
                .address(schedule.getAddress()) // ✅ 반영
                .build();
    }

    public Schedule toEntity() {
        return Schedule.builder()
                .title(title)
                .mapx(mapx)
                .mapy(mapy)
                .orderIndex(orderIndex)
                .description(description)
                .imgUrl(imgUrl)
                .address(address) // ✅ 반영
                .build();
    }
}
