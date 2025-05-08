package com.example.borrowit.controller;

import com.example.borrowit.Dto.DiscountDTO;
import com.example.borrowit.Entity.Discount;
import com.example.borrowit.repository.DiscountRepository;
import com.example.borrowit.service.impl.DiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/discounts")
@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class DiscountController {
    @Autowired

    private  DiscountService discountService;
    private DiscountRepository discountRepository;
    // Create a new discount
   @PostMapping("/add-discounts")
    public DiscountDTO createDiscount(@RequestBody DiscountDTO discountDto) {
        Discount createdDiscount = discountService.createDiscount(discountDto);
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
    // Route pour obtenir les discounts actifs pour un item
    /*@GetMapping("/item/{itemId}/active")
    public List<DiscountDTO> getActiveDiscountsForItem(@PathVariable Long itemId) {
        List<Discount> discounts = discountService.getActiveDiscountsForItem(itemId);
        return discounts.stream()
                .map(this::mapToDiscountDTO)
                .collect(Collectors.toList());
    }*/
    // Méthode pour obtenir les discounts associés à un item spécifique
    @GetMapping("/itemactive/{itemId}/{active}")
    public ResponseEntity<List<Discount>> getActiveDiscountsForItem(
            @PathVariable Long itemId,
            @PathVariable boolean active) {

        List<Discount> discounts = discountService.getDiscountsForItemAndActiveStatus(itemId, active);

        if (discounts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok(discounts);
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
        discountDTO.setItem_id(discount.getItem() != null ? discount.getItem().getId() : null);
        return discountDTO;
    }
}
