package com.example.borrowit.controller;

import com.example.borrowit.Entity.Item;
import com.example.borrowit.service.impl.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/items")
public class ItemController {
    @Autowired
    private ItemService itemService = new ItemService();
    @GetMapping
    public List<Item> getAllItems() {
        return itemService.getAllItems();
    }
    @GetMapping("/{id}")
    public Item getItemById(@PathVariable Long id) {
        return itemService.getItemById(id) ;
    }
}
