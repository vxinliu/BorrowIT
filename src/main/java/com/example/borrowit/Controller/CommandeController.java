package com.example.borrowit.controller;

import com.example.borrowit.Dto.CommandeRequest;
import com.example.borrowit.Entity.*;
import com.example.borrowit.service.*;
import com.example.borrowit.service.impl.CommandeServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/commandes")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class CommandeController {

    @Autowired
    private CommandeService commandeService;
    @Autowired
    private CommandeServiceImpl commandeServiceImpl;

    @PostMapping("/add-commandes")
    public ResponseEntity<Commande> createCommande(@RequestBody CommandeRequest request) {
        Commande commande = commandeService.createCommande(
                request.getItemId(),
                request.getDescription()

        );
        return ResponseEntity.status(HttpStatus.CREATED).body(commande);
    }
    @GetMapping("/item/{id}")
    public ResponseEntity<Item> getItemByCommandeId(@PathVariable Long id) {
        Item item = commandeServiceImpl.getItemByCommandeId(id);
        if (item != null) {
            return ResponseEntity.ok(item);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/owner/{ownerId}")
    public List<Commande> getCommandesPourProprietaire(@PathVariable Long ownerId) {
        return commandeServiceImpl.getCommandesByItemOwner(ownerId);
    }
    @GetMapping("/borrower/{id}")
    public ResponseEntity<User> getBorrowerByCommandeId(@PathVariable Long id) {
        User borrower = commandeServiceImpl.getBorrowerByCommandeId(id);
        if (borrower != null) {
            return ResponseEntity.ok(borrower);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/get-commandes")
    public List<Commande> getAllCommandes() {
        return commandeService.getAllCommandes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Commande> getCommandeById(@PathVariable Long id) {
        Commande commande = commandeService.getCommandeById(id);

        if (commande == null) {
            return ResponseEntity.notFound().build();  // Retourner 404 si commande n'existe pas
        }

        return ResponseEntity.ok(commande);  // Retourner 200 OK avec la commande
    }

    /*// CommandeController.java
    @PutMapping("/confirm/{id}")
    public ResponseEntity<String> confirmCommande(@PathVariable Long id) {
        commandeService.confirmCommande(id);
        return ResponseEntity.ok("Commande confirmée et SMS envoyé !");
    }*/

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




