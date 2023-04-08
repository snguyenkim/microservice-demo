package com.example.inventoryservice.inventory;

import lombok.*;

@Builder
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InventoryResponse {
  private String sku;
  private String name;
  private Integer quantity;

}
