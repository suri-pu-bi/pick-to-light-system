package com.mobicom.swcapstone.domain;


import lombok.*;

import javax.persistence.*;


@Table(name = "ORDER_PRODUCT")
@Getter
@Entity
// 3개를 같이 써야하는 이유
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class OrderProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_product_id", columnDefinition = "BIGINT(11)")
    private Long id;

    @Column(nullable = false, columnDefinition = "INT(11)")
    private Long orderCount;

    @ManyToOne
    @JoinColumn(name="order_id", nullable = false)
    private Orders order;

    @ManyToOne
    @JoinColumn(name="product_id", nullable = false)
    private Product product;






}