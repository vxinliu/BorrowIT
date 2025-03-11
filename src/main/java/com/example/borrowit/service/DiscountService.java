package com.example.borrowit.service;

import com.example.borrowit.Entity.Discount;
import java.util.*;

public interface DiscountService {
    List<Discount> getAllDiscounts();
    Discount getDiscountById(Long id);
    Discount saveDiscount(Discount discount);
    void deleteDiscount(Long id);
    boolean isDiscountValid(Long id);
    List<Discount> getActiveDiscounts();
    List<Discount> getDiscountsByDate(Date date);
    List<Discount> getDiscountsByMinPercentage(float percentage);

}
