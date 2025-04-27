package com.example.borrowit.repository;

import com.example.borrowit.Entity.Commande;
import com.example.borrowit.Entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommandeRepository  extends JpaRepository<Commande, Long> {
    @Query("SELECT c FROM Commande c WHERE c.item.owner.id = :userId")
    List<Commande> findByItemOwnerId(@Param("userId") Long userId);
    @Query("SELECT c FROM Commande c JOIN FETCH c.item i JOIN FETCH i.owner o WHERE o.id = :ownerId")
    List<Commande> findCommandesByItemOwnerId(@Param("ownerId") Long ownerId);


}
