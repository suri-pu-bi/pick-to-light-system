package com.mobicom.swcapstone.domain;


import lombok.*;

import javax.persistence.*;
import java.util.Date;


@Table(name = "PRODUCT")
@Getter
@Setter
@Entity
// 3개를 같이 써야하는 이유
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", columnDefinition = "BIGINT(11)")
    private Long id;

    @Column(nullable = false, columnDefinition = "VARCHAR(50)")
    private String name;

    @Column(nullable = false, columnDefinition = "INT(11)")
    private Long productCount;

    @Column(nullable = false, columnDefinition = "INT(20)")
    private Long price;

    @Column(nullable = false, columnDefinition = "VARCHAR(50)")
    private String location;













}