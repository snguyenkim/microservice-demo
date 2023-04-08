package com.example.inventoryservice.util;

import com.example.inventoryservice.inventory.Inventory;
import com.example.inventoryservice.inventory.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final InventoryRepository inventoryRepository;
    @Override
    public void run(String... args) throws Exception {
        Inventory inventory = new Inventory();
        inventory.setSku("iphone_13");
        inventory.setName("iPhone 13");
        inventory.setQuantity(10);

        Inventory inventory1 = new Inventory();
        inventory1.setSku("iphone_13_red");
        inventory1.setName("iPhone 13 Red");
        inventory1.setQuantity(0);

        Inventory inventory2 = new Inventory();
        inventory2.setSku("galaxy_22");
        inventory2.setName("Samsung Galaxy 22");
        inventory2.setQuantity(5);

        Inventory inventory3 = new Inventory();
        inventory3.setSku("galaxy_22_ultra");
        inventory3.setName("Samsung Galaxy 22 Ultra");
        inventory3.setQuantity(5);

        Inventory inventory4 = new Inventory();
        inventory4.setSku("game-001");
        inventory4.setName("X-Plane 11");
        inventory4.setQuantity(5);

        Inventory inventory5 = new Inventory();
        inventory5.setSku("monitor-01");
        inventory5.setName("Alien 34 inches Curved Monitor");
        inventory5.setQuantity(5);

        Inventory inventory6 = new Inventory();
        inventory6.setSku("monitor-02");
        inventory6.setName("Dell 34 inches Curved Monitor");
        inventory6.setQuantity(5);

        List<Inventory> inventoryList = Arrays.asList(
            inventory, inventory1, inventory2, inventory3,
            inventory4, inventory5, inventory6);
        inventoryRepository.saveAll(inventoryList);

    }
}
