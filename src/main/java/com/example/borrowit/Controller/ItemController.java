package com.example.borrowit.Controller;

import com.example.borrowit.Entity.Item;
import com.example.borrowit.repository.ItemRepository;
import com.example.borrowit.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemService itemService;

    // Récupérer tous les items
    @GetMapping("/get-items")
    public List<Item> getAllItems() {
        return itemRepository.findAll(); // Assure-toi que ça fonctionne correctement
    }
    @GetMapping("/get-items/{id}")
    public Item getItemById(@PathVariable Long id) {
        return itemRepository.getItemById(id);
    }
    @PostMapping("/add-items") // attention au / manquant !
    public ResponseEntity<Item> createItem(@RequestBody Item item) {
        Item savedItem = itemService.save(item); // ✅ Utilise maintenant le service qui fonctionne
        return ResponseEntity.ok(savedItem);
    }
}
