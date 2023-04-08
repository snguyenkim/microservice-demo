package com.example.orderservice.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
  private List<OrderLineItemsDto> orderLineItemsDtoList;

  public Order toOrder() {
    return Order.builder()
        .orderNumber(UUID.randomUUID().toString())
        .orderLineItemsList(
            this.orderLineItemsDtoList.stream()
                .map(OrderLineItemsDto::toOrderLineItems)
                .collect(Collectors.toList())
        )
        .build();
  }

  public List<String> getSkuList() {
    return this.orderLineItemsDtoList.stream()
        .map(item -> item.getSku())
        .collect(Collectors.toList());
  }
}
