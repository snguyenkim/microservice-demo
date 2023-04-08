package com.example.inventoryservice.inventory;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class InventoryController {
  private final InventoryService inventoryService;

  @GetMapping("/inventory")
  public List<Inventory> getAll() {
    return inventoryService.getAll();
  }

  @GetMapping("/inventory/{id}")
  public Inventory findById(@PathVariable Long id) {
    return inventoryService.findById(id);
  }

  // http://localhost:9001/api/inventory/sku-list-path/iphone-001,game-001
  @GetMapping("/inventory-sku-list-path/{skus}")
  public List<InventoryResponse> findBySkuListPath(@PathVariable("skus") List<String> skuList) {
    return inventoryService.findBySkuList(skuList);
  }

  // http://localhost:9001/api/inventory/sku-list-request?skus=iphone-001,game-001
  @GetMapping("/inventory-sku-list-request")
  public List<InventoryResponse> findBySkuListRequest(@RequestParam("skus") List<String> skuList) {
    return inventoryService.findBySkuList(skuList);
  }

  @PostMapping("/inventory")
  public Inventory save(@RequestBody InventoryRequest inventoryRequest) {
    return inventoryService.save(inventoryRequest);
  }

  @PostMapping("/inventory-batch")
  public List<Inventory> saveAll(@RequestBody List<InventoryRequest> inventoryRequestList) {
    return inventoryService.saveAllByInventoryRequest(inventoryRequestList);
  }

  @PostMapping("/inventory-is-in-stock")
  public Boolean findByIdList(@RequestBody List<InventoryRequest> inventoryRequestList) {
    return inventoryService.isInStock(inventoryRequestList);
  }

}
