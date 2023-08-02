package com.mobicom.swcapstone.dto.response;

import com.mobicom.swcapstone.domain.Orders;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private Long orderId;

    private String userId;

    private LocalDate date;

    public OrderResponse(Orders orders){
        this.orderId = orders.getId();
        this.userId = orders.getUser().getUserId();
        this.date = orders.getDate();
    }
}
