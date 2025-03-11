package com.example.borrowit.service.Impl;

import com.example.borrowit.Entity.*;
import com.example.borrowit.repository.*;
import com.example.borrowit.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DiscountServiceImpl implements DiscountService {

    @Autowired
    private DiscountRepository discountRepository;

    @Override
    public List<Discount> getAllDiscounts() {
        return discountRepository.findAll();
    }

    @Override
    public Discount getDiscountById(Long id) {
        return discountRepository.findById(id).orElse(null);
    }

    @Override
    public Discount saveDiscount(Discount discount) {
        return discountRepository.save(discount);
    }

    @Override
    public void deleteDiscount(Long id) {
        discountRepository.deleteById(id);
    }
    @Override
    public boolean isDiscountValid(Long id) {
        Discount discount = getDiscountById(id);
        Date now = new Date();
        return discount != null && discount.isActive() && now.after(discount.getStartDate()) && now.before(discount.getEndDate());
    }

    @Override
    public List<Discount> getActiveDiscounts() {
        return discountRepository.findByActiveTrue();
    }
    @Override
    public List<Discount> getDiscountsByDate(Date date) {
        return discountRepository.findByStartDateBeforeAndEndDateAfter(new Date(),new Date());
    }

    @Override
    public List<Discount> getDiscountsByMinPercentage(float percentage) {
        return discountRepository.findByPercentageGreaterThanEqual(percentage);
    }


}
