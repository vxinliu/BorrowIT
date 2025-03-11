package com.example.borrowit.repository;

import com.example.borrowit.Entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
    List<Discount> findByStartDateBeforeAndEndDateAfter(Date startDate, Date endDate);
    List<Discount> findByPercentageGreaterThanEqual(float percentage);
    List<Discount> findByActiveTrue();

}