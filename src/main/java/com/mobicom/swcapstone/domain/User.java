package com.mobicom.swcapstone.domain;


import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;


@Table(name = "USER")
@Getter
@Entity
// 3개를 같이 써야하는 이유
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private String userId; // id


    @Column(nullable = false)
    private String password;

    // enum 타입을 엔티티 클래스 속성으로 사용 , name이 DB에 저장
    @Enumerated(EnumType.STRING)
    private Role role; // 권한: manager, operator






}