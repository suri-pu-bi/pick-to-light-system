package com.mobicom.swcapstone.controller;


import com.mobicom.swcapstone.dto.request.OrderRequest;
import com.mobicom.swcapstone.dto.response.OrderProductResponse;
import com.mobicom.swcapstone.dto.response.OrderResponse;
import com.mobicom.swcapstone.dto.response.ProductResponse;
import com.mobicom.swcapstone.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/manager/products")
    public ResponseEntity<List<ProductResponse>> getProducts(){
        return new ResponseEntity<>(orderService.getProducts(), HttpStatus.OK);
    }

    @PostMapping("/manager/order")
    public ResponseEntity<List<OrderProductResponse>> registerOrder(@AuthenticationPrincipal UserDetails userDetails, @RequestBody List<OrderRequest> request) throws Exception {
        String userId = userDetails.getUsername();
        return new ResponseEntity<>(orderService.registerOrder(userId, request), HttpStatus.OK);
    }
}
