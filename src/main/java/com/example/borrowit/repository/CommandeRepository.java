package com.example.borrowit.repository;

import com.example.borrowit.Entity.Commande;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface CommandeRepository extends JpaRepository<Commande, Long> {
    //List<Commande> findByUserId(Long userId);

}