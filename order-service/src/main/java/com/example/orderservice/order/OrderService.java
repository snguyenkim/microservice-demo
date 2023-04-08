package com.example.orderservice.order;

import com.example.orderservice.config.WebClientConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {

    private final WebClient.Builder webClientBuilder;
    private final OrderRepository orderRepository;

    public Order findById(Long id) {
        Order order = orderRepository.findById(id).get();
        return order;
    }

    public String placeOrder(OrderRequest orderRequest) {
        Order order = orderRequest.toOrder();

        //http://localhost:9001/api/inventory-sku-list-request?skus=iphone-001,game-001
        Stream<InventoryResponse> inventoryResponseList = Arrays.stream(webClientBuilder.build().get()
            .uri("http://localhost:9001/api/inventory-sku-list-request",
                uriBuilder -> uriBuilder.queryParam("skus", orderRequest.getSkuList()).build())
            .retrieve()
            .bodyToMono(InventoryResponse[].class)
            .block());

        boolean allProductsInStock = inventoryResponseList.count() == orderRequest.getSkuList().stream().count();

        if (allProductsInStock) {
            orderRepository.save(order);
            return "Order Placed";
        } else {
            throw new IllegalArgumentException("Product is not in stock, please try again later");
        }
    }

}
