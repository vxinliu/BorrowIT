package com.example.borrowit.service.impl;
import com.example.borrowit.Dto.ItemsDTO;
import com.example.borrowit.Entity.Category;
import com.example.borrowit.Entity.Item;
import com.example.borrowit.Entity.StatusItem;
import com.example.borrowit.Entity.User;
import com.example.borrowit.repository.CategoryRepository;
import com.example.borrowit.repository.ItemRepository;

import com.example.borrowit.repository.UserRepository;
import com.example.borrowit.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ItemServiceimpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;
    @Override
    public List<ItemsDTO> getAllItems() {
        List<Item> items= itemRepository.findAll();
        List< ItemsDTO> itemsDTOS =new ArrayList<>();
        for(Item item :items){
            ItemsDTO itemDto = new ItemsDTO();
            itemDto.setId(item.getId());
            itemDto.setItemCondition(item.getItemCondition());
            itemDto.setDescription(item.getDescription());
            itemDto.setImage(item.getImage());
            itemDto.setPrice(item.getPrice());
            itemDto.setName(item.getName());
            itemDto.setAvailability(item.getAv());
            itemDto.setCategoryType(item.getCategory().getName().toString());
            itemDto.setIdUser(item.getOwner().getId());
            itemDto.setStatusItem(item.getStatusItem());
            itemsDTOS.add(itemDto);
        }
        return itemsDTOS;
    }

    @Override
    public Optional<Item> getItemById(Long id) {
        return itemRepository.findById(id);
    }

    @Override
    public Item createItem(ItemsDTO itemdto,Long id) {
        Category category = categoryRepository.findById(id).get();
        User user = userRepository.findById(itemdto.getIdUser()).get();

        Item item = new Item();
        item.setName(itemdto.getName());
        item.setDescription(itemdto.getDescription());
        item.setItemCondition(itemdto.getItemCondition());
        item.setAvailability(itemdto.isAvailability());
        item.setPrice(itemdto.getPrice());
        item.setImage(itemdto.getImage());
        item.setCategory(category);
        item.setOwner(user);
        item.setStatusItem(StatusItem.PENDING);
        return itemRepository.save(item);
    }

    @Override
    public Item updateItem(Long id, Item itemDetails,Long idCategory) {
        Optional<Item> existingItem = itemRepository.findById(id);
        Category existingCategory = categoryRepository.findById(idCategory).get();
        if (existingItem.isPresent()) {
            Item item = existingItem.get();
            item.setName(itemDetails.getName());
            item.setDescription(itemDetails.getDescription());
            item.setItemCondition(itemDetails.getItemCondition());
            item.setAvailability(itemDetails.isAvailability());
            item.setPrice(itemDetails.getPrice()); // Mise à jour du prix
            item.setImage(itemDetails.getImage()); // Mise à jour de l'image
            item.setCategory(existingCategory);
            return itemRepository.save(item);
        }
        return null;
    }

    @Override
    public void updateStatusItem(Long idItem, StatusItem statusItem) {
        Item existingItem = itemRepository.findById(idItem).get();
        existingItem.setStatusItem(statusItem);
        itemRepository.save(existingItem);
        }

    @Override
    public boolean deleteItem(Long id) {
        if (itemRepository.existsById(id)) {
            itemRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
