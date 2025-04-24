package com.example.borrowit.repository;

import com.example.borrowit.Entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
    Optional<Discount> findByItemIdAndActiveTrue(Long id);
    Optional<Discount> findFirstByItemIdAndActiveTrue(Long itemId);

    List<Discount> findByItemIdAndActive(Long itemId, boolean active);
}
