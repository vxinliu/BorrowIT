package com.example.borrowit.service.impl;

import com.example.borrowit.Entity.Commande;
import com.example.borrowit.repository.CommandeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommandeServiceImpl {
    @Autowired
    private CommandeRepository commandeRepository;
    public List<Commande>getCommandes(){
        return commandeRepository.findAll();
    }
    public List<Commande> findByUser(Long userId) {
        return commandeRepository.findByItemOwnerId(userId);
    }
    public List<Commande> getCommandesByItemOwner(Long ownerId) {
        return commandeRepository.findCommandesByItemOwnerId(ownerId);
    }


}
