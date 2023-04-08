package com.example.inventoryservice.inventory;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InventoryRequest {
  private String sku;
  private String name;
  private Integer quantity;

  public Inventory toInventory() {
    return Inventory.builder()
        .sku(this.sku)
        .name(this.name)
        .quantity(this.quantity)
        .build();
  }

}
