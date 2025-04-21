package com.example.borrowit.controller;

import com.example.borrowit.Entity.Item;
import com.example.borrowit.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items") // 👈 nom en minuscules pour cohérence
public class ItemController {

    @Autowired
    private ItemRepository itemRepository;

    // Récupérer tous les items
    @GetMapping("/get-items")
    public List<Item> getAllItems() {
        return itemRepository.findAll(); // Assure-toi que ça fonctionne correctement
    }
}
