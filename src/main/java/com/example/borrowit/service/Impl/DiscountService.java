package com.example.borrowit.service.Impl;

import com.example.borrowit.DTO.DiscountDTO;
import com.example.borrowit.Entity.Discount;
import com.example.borrowit.Entity.Item;
import com.example.borrowit.repository.DiscountRepository;
import com.example.borrowit.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiscountService {
    @Autowired
    private  DiscountRepository discountRepository;
    @Autowired
    private ItemRepository itemRepository;

    // Create a new discount
    public Discount createDiscount(DiscountDTO discountDTO) {
        Discount discount = new Discount();
        discount.setName(discountDTO.getName());
        discount.setCode(discountDTO.getCode());
        discount.setPercentage(discountDTO.getPercentage());
        discount.setStartDate(discountDTO.getStartDate());
        discount.setEndDate(discountDTO.getEndDate());
        discount.setActive(discountDTO.isActive());
        // Récupérer l'item et l'associer
        Item item = itemRepository.findById(discountDTO.getItem_id())
                .orElseThrow(() -> new RuntimeException("Item not found with ID: " + discountDTO.getItem_id()));
        discount.setItem(item);        return discountRepository.save(discount);
    }

    // Get discount by ID
    public Discount getDiscountById(Long id) {
        return discountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Discount not found"));
    }

    // Get all discounts
    public List<Discount> getAllDiscounts() {
        return discountRepository.findAll();
    }

    // Update an existing discount
    public Discount updateDiscount(Long id, DiscountDTO discountDTO) {
        Discount discount = discountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Discount not found"));

        discount.setName(discountDTO.getName());
        discount.setCode(discountDTO.getCode());
        discount.setPercentage(discountDTO.getPercentage());
        discount.setStartDate(discountDTO.getStartDate());
        discount.setEndDate(discountDTO.getEndDate());
        discount.setActive(discountDTO.isActive());
        // Mettre à jour l'item si nécessaire
        Item item = itemRepository.findById(discountDTO.getItem_id())
                .orElseThrow(() -> new RuntimeException("Item not found with ID: " + discountDTO.getItem_id()));
        discount.setItem(item);
        return discountRepository.save(discount);
    }

    // Delete a discount by ID
    public void deleteDiscount(Long id) {
        discountRepository.deleteById(id);
    }


    public List<Discount> getDiscountsForItemAndActiveStatus(Long itemId, boolean active) {
        return discountRepository.findByItemIdAndActive(itemId, active);
    }
}
