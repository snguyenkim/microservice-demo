package com.example.orderservice.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

  private final OrderService orderService;

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Order findById(@PathVariable Long id) {
    Order order = orderService.findById(id);
    return order;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
//  @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
//  @TimeLimiter(name = "inventory")
//  @Retry(name = "inventory")
  public String placeOrder(@RequestBody OrderRequest orderRequest) {
    log.info("Placing Order");
    return orderService.placeOrder(orderRequest);
  }

//  public CompletableFuture<String> fallbackMethod(OrderRequest orderRequest, RuntimeException runtimeException) {
//    log.info("Cannot Place Order Executing Fallback logic");
//    return CompletableFuture.supplyAsync(() -> "Oops! Something went wrong, please order after some time!");
//  }
}