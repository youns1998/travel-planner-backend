package com.travelplanner.backend.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private String nickname;

    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    // ✅ 출발지 주소 및 좌표 저장 필드
    private String originAddress;
    private Double originX;
    private Double originY;
}
