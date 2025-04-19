package com.example.borrowit.service.impl;

import com.example.borrowit.Entity.Item;
import com.example.borrowit.service.IItemService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements IItemService {
    @Override
    public List<Item> retrieveAllItems() {
        return null;
    }

    @Override
    public Item retrieveItem(Long itemId) {
        return null;
    }

    @Override
    public Item addItem(Item i) {
        return null;
    }

    @Override
    public void removeItem(Long itemId) {

    }

    @Override
    public Item modifyItem(Item i) {
        return null;
    }
}
