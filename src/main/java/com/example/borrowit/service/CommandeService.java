package com.example.borrowit.service;

import com.example.borrowit.Entity.Commande;
import java.util.*;

public interface CommandeService {
    List<Commande> getAllCommandes();
    Commande getCommandeById(Long id);
    Commande saveCommande(Commande commande);
    void deleteCommande(Long id);
    double calculateTotalPrice(Long commandeId);
    Commande applyDiscountToCommande(Long commandeId, Long discountId);
    //List<Commande> getCommandesByUser(Long userId);
    List<Commande> getCommandesByDateRange(Date start, Date end);
    public Commande updateCommande(Long id, Commande updatedCommande);

}