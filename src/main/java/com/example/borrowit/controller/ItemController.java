package com.example.borrowit.controller;

import com.example.borrowit.Dto.ItemsDTO;
import com.example.borrowit.Entity.Item;
import com.example.borrowit.Entity.StatusItem;
import com.example.borrowit.configuration.EmailServiceStatusUpdate;
import com.example.borrowit.repository.ItemRepository;
import com.example.borrowit.service.impl.ItemServiceimpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/items")

public class ItemController {

    @Autowired
    private ItemServiceimpl itemService;
    @Autowired
    private EmailServiceStatusUpdate emailServiceStatusUpdate;
    @Autowired
    private ItemRepository itemRepository;

    @GetMapping("/All")
    public List<ItemsDTO> getAllItems() {
        return itemService.getAllItems();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable Long id) {
        Optional<Item> item = itemService.getItemById(id);
        return item.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/add/{id}")
    public ResponseEntity<Item> createItem(@RequestBody ItemsDTO item,@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.CREATED).body(itemService.createItem(item,id));
    }

    @PutMapping("/edit/{id}/{idCategory}")
    public ResponseEntity<Item> updateItem(@PathVariable Long id, @RequestBody Item item,@PathVariable Long idCategory) {
        Item updatedItem = itemService.updateItem(id, item,idCategory);
        return updatedItem != null ? ResponseEntity.ok(updatedItem) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    @PutMapping("/updateStatus/{id}/{statusItem}")
    public void updateStatusItem(@PathVariable Long id,@PathVariable StatusItem statusItem) {
        itemService.updateStatusItem(id, statusItem); // update logic
        Item item = itemRepository.findById(id).get();

        String userEmail = item.getOwner().getEmail(); // assuming an Item is linked to a User

        emailServiceStatusUpdate.sendStatusUpdateEmail(userEmail, item);
        System.out.println(userEmail);

    }



    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        return itemService.deleteItem(id) ? ResponseEntity.noContent().build() : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }}