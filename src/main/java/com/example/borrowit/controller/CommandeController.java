package com.example.borrowit.controller;

import com.example.borrowit.Entity.*;
import com.example.borrowit.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/commandes")
public class CommandeController {

    @Autowired
    private CommandeService commandeService;

    @GetMapping
    public List<Commande> getAllCommandes() {
        return commandeService.getAllCommandes();
    }

    @GetMapping("/{id}")
    public Commande getCommandeById(@PathVariable Long id) {
        return commandeService.getCommandeById(id);
    }

    @PostMapping
    public Commande createCommande(@RequestBody Commande commande) {
        return commandeService.saveCommande(commande);
    }

    @PutMapping("/{id}")
    public Commande updateCommande(@PathVariable Long id, @RequestBody Commande updatedCommande) {
        updatedCommande.setId(id);
        return commandeService.updateCommande(id,updatedCommande);
    }

    @DeleteMapping("/{id}")
    public void deleteCommande(@PathVariable Long id) {
        commandeService.deleteCommande(id);
    }
    // Appliquer une réduction à une commande
    @PutMapping("/{commandeId}/discount/{discountId}")
    public Commande applyDiscountToCommande(@PathVariable Long commandeId, @PathVariable Long discountId) {
        return commandeService.applyDiscountToCommande(commandeId, discountId);
    }

    // Calculer le prix total avec remise appliquée
    @GetMapping("/{commandeId}/calculate-total")
    public double calculateTotalPrice(@PathVariable Long commandeId) {
        return commandeService.calculateTotalPrice(commandeId);
    }

    // Rechercher les commandes par intervalle de dates
    @GetMapping("/search-by-date")
    public List<Commande> getCommandesByDateRange(@RequestParam Date startDate, @RequestParam Date endDate) {
        return commandeService.getCommandesByDateRange(startDate, endDate);
    }

    // Rechercher les commandes d’un utilisateur
    //@GetMapping("/user/{userId}")
    //public List<Commande> getCommandesByUser(@PathVariable Long userId) {
    //   return commandeService.getCommandesByUser(userId);
    //}
}
