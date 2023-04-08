package com.example.inventoryservice.inventory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryService {
  private final InventoryRepository inventoryRepository;

  public List<Inventory> getAll() {
    return inventoryRepository.findAll();
  }

  public Inventory findById(Long id) {
    return inventoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Not found!"));
  }

//  public List<InventoryResponse> isInStock(List<String> skuCode) {
//    log.info("Checking Inventory");
//    return inventoryRepository.findBySkuCodeIn(skuCode).stream()
//        .map(inventory ->
//            InventoryResponse.builder()
//                .skuCode(inventory.getSkuCode())
//                .isInStock(inventory.getQuantity() > 0)
//                .build()
//        ).toList();
//  }

  public Inventory save(InventoryRequest inventoryRequest) {
    return inventoryRepository.save(inventoryRequest.toInventory());
  }

  public List<Inventory> saveAll(List<Inventory> inventoryList) {
    return inventoryRepository.saveAll(inventoryList);
  }

  public List<Inventory> saveAllByInventoryRequest(List<InventoryRequest> inventoryRequest) {
    List<Inventory> inventoryList = inventoryRequest.stream()
        .map(InventoryRequest::toInventory)
        .collect(Collectors.toList());
    return inventoryRepository.saveAll(inventoryList);
  }

  public List<Inventory> findByIdList(List<Long> idList) {
    return inventoryRepository.findByIdIn(idList);
  }

  public List<InventoryResponse> findBySkuList(List<String> skuList) {
    return inventoryRepository.findBySkuIn(skuList).stream()
        .map(Inventory::toInventoryResponse)
        .collect(Collectors.toList());
  }

  public Boolean isInStock(List<InventoryRequest> inventoryRequestList) {
    List<String> skuList = inventoryRequestList.stream()
        .map(InventoryRequest::getSku)
        .collect(Collectors.toList());
    List<InventoryResponse> inventoryList = findBySkuList(skuList);
    return inventoryRequestList.stream()
        .allMatch(item -> inventoryList.stream().filter(it ->
            (it.getSku().equals(item.getSku()) && it.getQuantity() >= item.getQuantity())).count()==1
        );
  }

}
