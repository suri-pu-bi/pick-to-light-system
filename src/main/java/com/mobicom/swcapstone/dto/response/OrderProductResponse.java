package com.mobicom.swcapstone.dto.response;

import com.mobicom.swcapstone.domain.Orders;
import com.mobicom.swcapstone.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderProductResponse {
    private Long id;

    private Long orderCount;

    private Orders order;

    private Product product;
}
