package com.example.orderservice.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderLineItemsDto {
  private String sku;
  private Integer quantity;

  public OrderLineItems toOrderLineItems() {
    return OrderLineItems.builder()
        .sku(this.sku)
        .quantity(this.quantity)
        .build();
  }
}
