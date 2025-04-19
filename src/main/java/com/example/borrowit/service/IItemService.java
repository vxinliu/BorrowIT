package com.example.borrowit.service;

import com.example.borrowit.Entity.Item;

import java.util.List;

public interface IItemService {
    public List<Item> retrieveAllItems();
    public Item retrieveItem(Long itemId);
    public Item addItem(Item i);
    public void removeItem(Long itemId);
    public Item modifyItem(Item i);

    // Here we will add later methods calling keywords and methods calling JPQL
}
