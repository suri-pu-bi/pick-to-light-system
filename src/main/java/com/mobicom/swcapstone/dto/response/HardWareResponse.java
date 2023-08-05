package com.mobicom.swcapstone.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HardWareResponse {

    private String name;

    private String location;

    private Long orderCount;
}
