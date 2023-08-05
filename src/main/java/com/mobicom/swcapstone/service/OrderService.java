package com.mobicom.swcapstone.service;

import com.mobicom.swcapstone.domain.OrderProduct;
import com.mobicom.swcapstone.domain.Orders;
import com.mobicom.swcapstone.domain.Product;
import com.mobicom.swcapstone.domain.User;
import com.mobicom.swcapstone.dto.request.OrderRequest;
import com.mobicom.swcapstone.dto.response.*;
import com.mobicom.swcapstone.repository.OrderProductRepository;
import com.mobicom.swcapstone.repository.OrderRepository;
import com.mobicom.swcapstone.repository.ProductRepository;
import com.mobicom.swcapstone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.aspectj.weaver.ast.Or;
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


    // Manager
    // 주문이 들어오고 order table에 등록
    public List<OrderProductResponse> registerOrder (String userId, List<OrderRequest> request) throws Exception {

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new Exception("계정을 찾을 수 없습니다."));

        // 주문 정보를 order table에 저장

        Orders orders = Orders.builder()
                .user(user)
                .date(LocalDate.now())
                .build();

        orderRepository.save(orders);

        // 들어온 주문 상품 목록을 product 상품 테이블에서 하나씩 조회해서 주문상품목록에 저장
        // 애초에 모바일에서 재고 개수보다 주문 물품 개수가 넘지 않게 들어옴
        // -> 바로 재고 개수에서 주문 물품 개수를 빼서 재고 업데이트

        List<OrderProductResponse> orderProductResponses = new ArrayList<>();
        for(OrderRequest orderRequest : request) {
            Product product = productRepository.findById(orderRequest.getProductId())
                            .orElseThrow( () -> new Exception("해당 상품을 찾을 수 없습니다."));

            OrderProduct orderProduct = OrderProduct.builder()
                    .orderCount(orderRequest.getCount())
                    .order(orders)
                    .product(product)
                    .build();

            orderProductRepository.save(orderProduct);

            orderProductResponses.add(OrderProductResponse.builder()
                    .id(orderProduct.getId())
                    .orderCount(orderProduct.getOrderCount())
                    .order(orderProduct.getOrder())
                    .product(orderProduct.getProduct()).build());


            Long updateCnt = product.getProductCount() - orderProduct.getOrderCount();

            product.setProductCount(updateCnt);
            productRepository.save(product);

        }


        return orderProductResponses;

    }

    // Operator
//    public List<HardWareResponse> getOrderProducts() {
//
//    }

}
