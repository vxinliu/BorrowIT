package com.example.borrowit.controller;

import com.example.borrowit.DTO.DiscountDTO;
import com.example.borrowit.Entity.Discount;
import com.example.borrowit.service.Impl.DiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/discounts")
@RequiredArgsConstructor
public class DiscountController {
    @Autowired

    private  DiscountService discountService;

    // Create a new discount
    @PostMapping("/add-discounts")
    public DiscountDTO createDiscount(@RequestBody DiscountDTO discountDTO) {
        Discount createdDiscount = discountService.createDiscount(discountDTO);
        return mapToDiscountDTO(createdDiscount);
    }

    // Get discount by ID
    @GetMapping("/{id}")
    public DiscountDTO getDiscountById(@PathVariable Long id) {
        Discount discount = discountService.getDiscountById(id);
        return mapToDiscountDTO(discount);
    }

    // Get all discounts
    @GetMapping("/get-discounts")
    public List<DiscountDTO> getAllDiscounts() {
        List<Discount> discounts = discountService.getAllDiscounts();
        return discounts.stream()
                .map(this::mapToDiscountDTO)
                .collect(Collectors.toList());
    }

    // Update an existing discount
    @PutMapping("/{id}")
    public DiscountDTO updateDiscount(@PathVariable Long id, @RequestBody DiscountDTO discountDTO) {
        Discount updatedDiscount = discountService.updateDiscount(id, discountDTO);
        return mapToDiscountDTO(updatedDiscount);
    }

    // Delete a discount by ID
    @DeleteMapping("/{id}")
    public void deleteDiscount(@PathVariable Long id) {
        discountService.deleteDiscount(id);
    }

    // Convert Discount entity to DiscountDTO
    private DiscountDTO mapToDiscountDTO(Discount discount) {
        DiscountDTO discountDTO = new DiscountDTO();
        discountDTO.setId(discount.getId());
        discountDTO.setName(discount.getName());
        discountDTO.setCode(discount.getCode());
        discountDTO.setPercentage(discount.getPercentage());
        discountDTO.setStartDate(discount.getStartDate());
        discountDTO.setEndDate(discount.getEndDate());
        discountDTO.setActive(discount.isActive());
        return discountDTO;
    }
}
