package com.example.borrowit.controller;

import com.example.borrowit.Entity.Commande;
import com.example.borrowit.service.impl.CommandeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/commandes")
public class CommandeController {
    @Autowired
    private CommandeServiceImpl commandeService;
    @GetMapping
    public List<Commande> getCommandes() {
        return commandeService.getCommandes();
    }
    @GetMapping("/owner/{ownerId}")
    public List<Commande> getCommandesPourProprietaire(@PathVariable Long ownerId) {
        return commandeService.getCommandesByItemOwner(ownerId);
    }


}
