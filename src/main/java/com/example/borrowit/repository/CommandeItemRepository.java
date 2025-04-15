package com.example.borrowit.repository;

import com.example.borrowit.Entity.CommandeItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommandeItemRepository extends JpaRepository<CommandeItem, Long> {
}

