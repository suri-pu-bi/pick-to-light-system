package com.mobicom.swcapstone.domain;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;


@Table(name = "ORDERS")
@Getter
@Entity
// 3개를 같이 써야하는 이유
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", columnDefinition = "BIGINT(11)")
    private Long id;

    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;






}