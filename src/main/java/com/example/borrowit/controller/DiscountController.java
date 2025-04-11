package com.example.borrowit.controller;

import com.example.borrowit.Entity.Discount;
import com.example.borrowit.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/discounts")
public class DiscountController {

    @Autowired
    private DiscountService discountService;

    @GetMapping("/get-discounts")
    public List<Discount> getAllDiscounts() {
        return discountService.getAllDiscounts();
    }

    @GetMapping("/{id}")
    public Discount getDiscountById(@PathVariable Long id) {
        return discountService.getDiscountById(id);
    }

    @PostMapping("/add-discounts")
    public Discount createDiscount(@RequestBody Discount discount) {
        return discountService.saveDiscount(discount);
    }

    @PutMapping("/{id}")
    public Discount updateDiscount(@PathVariable Long id, @RequestBody Discount updatedDiscount) {
        updatedDiscount.setId(id);
        return discountService.saveDiscount(updatedDiscount);
    }

    @DeleteMapping("/{id}")
    public void deleteDiscount(@PathVariable Long id) {
        discountService.deleteDiscount(id);
    }
    // Vérifier si une remise est valide (date + active)
    @GetMapping("/{id}/is-valid")
    public boolean isDiscountValid(@PathVariable Long id) {
        return discountService.isDiscountValid(id);
    }

    // Récupérer les remises actives
    @GetMapping("/active")
    public List<Discount> getActiveDiscounts() {
        return discountService.getActiveDiscounts();
    }
    // Rechercher les discounts actifs à une date donnée
    //GET /api/discounts/active-at-date?date=2025-03-09
    @GetMapping("/active-at-date")
    public List<Discount> getDiscountsByDate(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date) {
        return discountService.getDiscountsByDate(date);
    }

    // Rechercher les discounts avec pourcentage minimum
    //GET /api/discounts/min-percentage?percentage=20
    @GetMapping("/min-percentage")
    public List<Discount> getDiscountsByMinPercentage(@RequestParam("percentage") float percentage) {
        return discountService.getDiscountsByMinPercentage(percentage);
    }


}