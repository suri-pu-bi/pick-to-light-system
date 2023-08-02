package com.mobicom.swcapstone.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OrderRequest {

    private Long productId;

    private Long count;
}
