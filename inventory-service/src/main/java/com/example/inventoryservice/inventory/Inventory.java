package com.example.inventoryservice.inventory;

import lombok.*;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="inventory")
public class Inventory {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(unique = true, nullable = false)
  private String sku;

  private String name;
  private Integer quantity;

  public InventoryResponse toInventoryResponse() {
    return InventoryResponse.builder()
        .sku(this.sku)
        .name(this.name)
        .quantity(this.quantity)
        .build();
  }
}
