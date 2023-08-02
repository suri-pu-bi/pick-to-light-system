package com.mobicom.swcapstone.service;

import com.mobicom.swcapstone.domain.Orders;
import com.mobicom.swcapstone.domain.Product;
import com.mobicom.swcapstone.domain.User;
import com.mobicom.swcapstone.dto.request.OrderRequest;
import com.mobicom.swcapstone.dto.response.LoginResponse;
import com.mobicom.swcapstone.dto.response.OrderResponse;
import com.mobicom.swcapstone.dto.response.ProductResponse;
import com.mobicom.swcapstone.repository.OrderProductRepository;
import com.mobicom.swcapstone.repository.OrderRepository;
import com.mobicom.swcapstone.repository.ProductRepository;
import com.mobicom.swcapstone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {

    private final OrderProductRepository orderProductRepository;

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    private final ProductRepository productRepository;


    // 상품 목록 가져오기
    public List<ProductResponse> getProducts() {
        List<Product> products = productRepository.findAll();

        List<ProductResponse> productResponses = new ArrayList<>();
        for (Product product : products) {
            productResponses.add(ProductResponse.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .productCount(product.getProductCount())
                    .price(product.getPrice())
                    .location(product.getLocation())
                    .build());

        }

        return productResponses;


    }



    // 주문 등록
    public OrderResponse registerOrder (String userId, List<OrderRequest> request) throws Exception {

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new Exception("계정을 찾을 수 없습니다."));

        // 주문 정보를 order table에 저장

        Orders orders = Orders.builder()
                .user(user)
                .date(LocalDate.now())
                .build();

        orderRepository.save(orders);

        return OrderResponse.builder()
                .orderId(orders.getId())
                .userId(orders.getUser().getUserId())
                .date(orders.getDate())
                .build();

    }

    //




}
