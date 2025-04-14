package com.example.borrowit.service.impl;

import com.example.borrowit.Entity.Item;
import com.example.borrowit.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ItemService {
@Autowired
    private ItemRepository itemRepository;
    public Item getItemById(Long id) {
        return itemRepository.findById(id).get();
    }
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }
}
