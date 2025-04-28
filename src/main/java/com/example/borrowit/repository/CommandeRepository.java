package com.example.borrowit.repository;

import com.example.borrowit.Entity.Commande;
import com.example.borrowit.Entity.Item;
import com.example.borrowit.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommandeRepository  extends JpaRepository<Commande, Long> {
    @Query("SELECT c FROM Commande c WHERE c.item.owner.id = :userId")
    List<Commande> findByItemOwnerId(@Param("userId") Long userId);
    @Query("SELECT c FROM Commande c JOIN FETCH c.item i JOIN FETCH i.owner o WHERE o.id = :ownerId")
    List<Commande> findCommandesByItemOwnerId(@Param("ownerId") Long ownerId);

    @Query("SELECT c FROM Commande c " +
            "LEFT JOIN FETCH c.item i " +
            "LEFT JOIN FETCH c.user b " +
            "WHERE c.id = :commandeId")
    Commande findByIdWithDetails(Long commandeId);
    @Query("SELECT c.item FROM Commande c WHERE c.id = :commandeId")
    Item findItemByCommandeId(Long commandeId);
    @Query("SELECT c.user FROM Commande c WHERE c.id = :commandeId")
    User findBorrowerByCommandeId(Long commandeId);

}
