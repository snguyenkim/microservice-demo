package com.example.orderservice.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;

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
        String uri = "http://localhost:9001/api/inventory-sku-list-request";
        String uri_discovery = "http://inventory-service/api/inventory-sku-list-request";

        /*
            TODO: Use Discovery Service
         */
        InventoryResponse[] inventoryResponses = webClientBuilder.build().get()
            .uri(uri_discovery,
                uriBuilder -> uriBuilder.queryParam("skus", orderRequest.getSkuList()).build())
            .header("Content-Type", "application/json-patch+json")
            .retrieve()
            .bodyToMono(InventoryResponse[].class)
            .block();

//        Stream<InventoryResponse> inventoryResponseList = Arrays.stream(webClientBuilder.build().get()
//            .uri(uri_discovery,
//                uriBuilder -> uriBuilder.queryParam("skus", orderRequest.getSkuList()).build())
//            .header("Content-Type", "application/json-patch+json")
//            .retrieve()
//            .bodyToMono(InventoryResponse[].class)
//            .block());

        boolean allProductsInStock = false;
        if (inventoryResponses != null) {
            allProductsInStock = Arrays.stream(inventoryResponses).count() == orderRequest.getSkuList().stream().count();
        }

        if (allProductsInStock) {
            orderRepository.save(order);
            return "Order Placed";
        } else {
            throw new IllegalArgumentException("Product is not in stock, please try again later");
        }
    }

}
