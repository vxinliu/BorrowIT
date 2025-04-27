package com.example.borrowit.service;

import com.example.borrowit.Dto.ItemsDTO;
import com.example.borrowit.Entity.Item;
import java.util.List;
import java.util.Optional;

public interface ItemService {
    List<ItemsDTO> getAllItems();
    Optional<Item> getItemById(Long id);
    Item createItem(ItemsDTO itemdto,Long id);
    Item updateItem(Long id, Item item,Long idCategory);
    boolean deleteItem(Long id);
}
