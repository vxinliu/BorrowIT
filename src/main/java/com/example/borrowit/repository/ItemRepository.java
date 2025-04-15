package com.example.borrowit.repository;

import com.example.borrowit.Entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
    // Tu peux ajouter des méthodes personnalisées ici si nécessaire
}
