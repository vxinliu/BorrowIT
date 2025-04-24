package com.example.borrowit.service;

import com.example.borrowit.DTO.CommandeDTO;
import com.example.borrowit.Entity.Commande;
import java.util.*;

public interface CommandeService {
        List<Commande> getAllCommandes();

        Commande createCommande(Long userId, Long itemId, String description);

        Commande getCommandeById(Long id);

        //void confirmCommande(Long id);
        //Commande getCommandeById(Long id);
        //Commande saveCommande(CreateCommandeRequest request);
        //void deleteCommande(Long id);
        //double calculateTotalPrice(Long commandeId);
        //Commande applyDiscountToCommande(Long commandeId, Long discountId);
        //List<Commande> getCommandesByDateRange(Date start, Date end);
        //Commande updateCommande(Long id, Commande updatedCommande);
        //public Commande convertToEntity(CommandeDTO dto);
        //public CommandeDTO convertToDto(Commande commande);
}


