package com.example.orderservice.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
  private String orderNumber;
  private List<OrderLineItemsDto> orderLineItemsDtoList;
}
