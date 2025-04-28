package com.example.borrowit.service.Impl;

import com.example.borrowit.Entity.Item;
import com.example.borrowit.repository.ItemRepository;
import com.example.borrowit.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ItemRepository itemRepository;


    @Override
    public Item save(Item item) {
        return itemRepository.save(item);

    }
}
