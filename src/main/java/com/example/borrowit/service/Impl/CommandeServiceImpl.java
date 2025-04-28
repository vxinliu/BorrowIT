package com.example.borrowit.service.impl;

import com.example.borrowit.Entity.Commande;
import com.example.borrowit.Entity.Discount;
import com.example.borrowit.Entity.Item;
import com.example.borrowit.Entity.User;
import com.example.borrowit.repository.*;
import com.example.borrowit.service.CommandeService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public Commande createCommande(Long itemId, String description) {
        Commande commande = new Commande();
        commande.setCreatedDate(new Date());
        commande.setStatus("EN ATTENTE");
        commande.setDescription(description);

        // Récupérer l'utilisateur connecté automatiquement PAR EMAIL
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // récupère l'email connecté

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur connecté non trouvé avec cet email"));
        commande.setUser(user);

        // Lier l'item
        if (itemId != null) {
            Item item = itemRepository.findById(itemId)
                    .orElseThrow(() -> new EntityNotFoundException("Item non trouvé"));

            double total = item.getPrice(); // Le prix de base de l'item

            // Appliquer discount actif
            Optional<Discount> discountOpt = discountRepository.findByItemIdAndActiveTrue(item.getId());
            if (discountOpt.isPresent()) {
                Discount discount = discountOpt.get();
                commande.setDiscount(discount);
                double discountValue = total * (discount.getPercentage() / 100.0); // Calcul de la réduction
                total -= discountValue; // Appliquer la réduction
            }

            commande.setTotalPrice(total); // Fixer le total après la réduction
            commande.setItem(item); // Lier l'item à la commande
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


