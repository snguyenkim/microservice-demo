package com.example.inventoryservice.inventory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
  List<Inventory> findByIdIn(List<Long> idList);

  List<Inventory> findBySkuIn(List<String> sku);

}
