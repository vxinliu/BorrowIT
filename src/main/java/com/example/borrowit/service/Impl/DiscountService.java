package com.example.borrowit.service.Impl;

import com.example.borrowit.DTO.DiscountDTO;
import com.example.borrowit.Entity.Discount;
import com.example.borrowit.repository.DiscountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiscountService {
    @Autowired

    private  DiscountRepository discountRepository;

    // Create a new discount
    public Discount createDiscount(DiscountDTO discountDTO) {
        Discount discount = new Discount();
        discount.setName(discountDTO.getName());
        discount.setCode(discountDTO.getCode());
        discount.setPercentage(discountDTO.getPercentage());
        discount.setStartDate(discountDTO.getStartDate());
        discount.setEndDate(discountDTO.getEndDate());
        discount.setActive(discountDTO.isActive());
        return discountRepository.save(discount);
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
        return discountRepository.save(discount);
    }

    // Delete a discount by ID
    public void deleteDiscount(Long id) {
        discountRepository.deleteById(id);
    }
}
