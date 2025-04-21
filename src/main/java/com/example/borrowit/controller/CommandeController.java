package com.example.borrowit.controller;

import com.example.borrowit.DTO.CreateCommandeRequest;
import com.example.borrowit.Entity.*;
import com.example.borrowit.service.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/commandes")
public class CommandeController {

    @Autowired
    private CommandeService commandeService;

    // Récupérer toutes les commandes
    @GetMapping("/get-commandes")
    public List<Commande> getAllCommandes() {
        return commandeService.getAllCommandes();
    }

    // Récupérer une commande par ID
    @GetMapping("/{id}")
    public Commande getCommandeById(@PathVariable Long id) {
        return commandeService.getCommandeById(id);
    }

    // Créer une nouvelle commande
    @PostMapping("/add-commandes")
    public ResponseEntity<?> createCommande(@RequestBody CreateCommandeRequest request) {
        try {
            // Vérifie si la liste des items est nulle
            if (request.getItems() == null || request.getItems().isEmpty()) {
                return ResponseEntity.badRequest().body("Erreur : la commande doit contenir au moins un item.");
            }

            Commande commande = commandeService.saveCommande(request);
            return ResponseEntity.ok(commande);

        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erreur : " + e.getMessage());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        }
    }


    // Mettre à jour une commande
    @PutMapping("/{id}")
    public Commande updateCommande(@PathVariable Long id, @RequestBody Commande updatedCommande) {
        return commandeService.updateCommande(id, updatedCommande);
    }

    // Supprimer une commande
    @DeleteMapping("/{id}")
    public void deleteCommande(@PathVariable Long id) {
        commandeService.deleteCommande(id);
    }

    // Appliquer une remise à une commande
    @PutMapping("/{commandeId}/discount/{discountId}")
    public Commande applyDiscountToCommande(@PathVariable Long commandeId, @PathVariable Long discountId) {
        return commandeService.applyDiscountToCommande(commandeId, discountId);
    }

    // Calculer le prix total avec la remise appliquée
    @GetMapping("/{commandeId}/calculate-total")
    public double calculateTotalPrice(@PathVariable Long commandeId) {
        return commandeService.calculateTotalPrice(commandeId);
    }

    // Rechercher les commandes par intervalle de dates
    @GetMapping("/search-by-date")
    public List<Commande> getCommandesByDateRange(@RequestParam Date startDate, @RequestParam Date endDate) {
        return commandeService.getCommandesByDateRange(startDate, endDate);
    }
/*
import com.example.borrowit.DTO.CommandeDTO;
import com.example.borrowit.DTO.CreateCommandeRequest;
import com.example.borrowit.Entity.Commande;
import com.example.borrowit.service.CommandeService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/commandes")
public class CommandeController {

    @Autowired
    private CommandeService commandeService;

    // ✅ Récupérer toutes les commandes (en DTO)
    @GetMapping
    public ResponseEntity<List<CommandeDTO>> getAllCommandes() {
        List<CommandeDTO> commandes = commandeService.getAllCommandes()
                .stream()
                .map(commandeService::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(commandes);
    }

    // ✅ Récupérer une commande par ID (DTO)
    @GetMapping("/{id}")
    public ResponseEntity<CommandeDTO> getCommandeById(@PathVariable Long id) {
        Commande commande = commandeService.getCommandeById(id);
        return ResponseEntity.ok(commandeService.convertToDto(commande));
    }

    // ✅ Créer une commande
    @PostMapping
    public ResponseEntity<CommandeDTO> createCommande(@RequestBody CreateCommandeRequest request) {
        Commande created = commandeService.saveCommande(request);
        return ResponseEntity.ok(commandeService.convertToDto(created));
    }

    // ✅ Mettre à jour une commande
    @PutMapping("/{id}")
    public ResponseEntity<CommandeDTO> updateCommande(@PathVariable Long id, @RequestBody Commande updatedCommande) {
        Commande updated = commandeService.updateCommande(id, updatedCommande);
        return ResponseEntity.ok(commandeService.convertToDto(updated));
    }

    // ✅ Supprimer une commande
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommande(@PathVariable Long id) {
        commandeService.deleteCommande(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ Appliquer une remise
    @PutMapping("/{commandeId}/discount/{discountId}")
    public ResponseEntity<CommandeDTO> applyDiscount(@PathVariable Long commandeId, @PathVariable Long discountId) {
        Commande commande = commandeService.applyDiscountToCommande(commandeId, discountId);
        return ResponseEntity.ok(commandeService.convertToDto(commande));
    }

    // ✅ Calculer le total avec remise
    @GetMapping("/{commandeId}/calculate-total")
    public ResponseEntity<Double> calculateTotal(@PathVariable Long commandeId) {
        double total = commandeService.calculateTotalPrice(commandeId);
        return ResponseEntity.ok(total);
    }

    // ✅ Chercher par date
    @GetMapping("/search-by-date")
    public ResponseEntity<List<CommandeDTO>> searchByDate(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {

        List<CommandeDTO> result = commandeService.getCommandesByDateRange(startDate, endDate)
                .stream()
                .map(commandeService::convertToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }*/
}



