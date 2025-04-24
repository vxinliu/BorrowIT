package com.example.borrowit.service.Impl;

import com.example.borrowit.Entity.Commande;
import com.example.borrowit.Entity.Discount;
import com.example.borrowit.Entity.Item;
import com.example.borrowit.Entity.User;
import com.example.borrowit.repository.*;
import com.example.borrowit.service.CommandeService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CommandeServiceImpl implements CommandeService {

    @Autowired
    private CommandeRepository commandeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private DiscountRepository discountRepository;

    @Override
    public Commande createCommande(Long itemId, Long userId, String description) {
        Commande commande = new Commande();
        commande.setCreatedDate(new Date());
        commande.setStatus("EN ATTENTE");
        commande.setDescription(description);


        // Lier l'utilisateur
        if (userId != null) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé"));
            commande.setUser(user);
        }

        // Lier l'item
        if (itemId != null) {
            Item item = itemRepository.findById(itemId)
                    .orElseThrow(() -> new EntityNotFoundException("Item non trouvé"));

            // Calculer le prix de base
            final double[] total = {item.getPrice()}; // Encapsuler le total dans un tableau

            // Chercher un discount actif lié à cet item
            Optional<Discount> discountOpt = discountRepository.findByItemIdAndActiveTrue(item.getId());
            discountOpt.ifPresent(discount -> {
                commande.setDiscount(discount);
                double discountValue = total[0] * (discount.getPercentage() / 100.0); // Utiliser total[0]
                total[0] -= discountValue; // Mettre à jour total[0]
            });

            commande.setTotalPrice(total[0]); // Utiliser total[0] après modification
        } else {
            throw new IllegalArgumentException("Item requis pour la commande.");
        }

        return commandeRepository.save(commande);
    }

    @Override
    public Commande getCommandeById(Long id) {
        // Recherche de la commande par ID
        return commandeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Commande non trouvée pour l'ID : " + id));
    }

    @Override
    public List<Commande> getAllCommandes() {
        return commandeRepository.findAll();
    }

    /*@Autowired
    private SmsService smsService;

    @Override
    public void confirmCommande(Long commandeId) {
        Commande commande = commandeRepository.findById(commandeId)
                .orElseThrow(() -> new EntityNotFoundException("Commande non trouvée"));

        commande.setStatus("CONFIRMÉ");
        commandeRepository.save(commande);

        // Envoi du SMS
        String phone = commande.getUser().getPhone();// Assure-toi que ce champ existe
        String message = "Bonjour " + commande.getUser().getName() +
                ", votre commande #" + commande.getId() + " est confirmée. Merci pour votre confiance ❤️";
        smsService.sendSms(phone, message);
    }*/
}


