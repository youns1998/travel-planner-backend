package com.travelplanner.backend.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private double mapx;
    private double mapy;
    private int orderIndex;

    @Lob
    @Column(columnDefinition = "CLOB")
    private String description; // 관광지 설명

    private String imgUrl; // 이미지 URL

    private String address; // ✅ 새로 추가된 주소 필드

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
