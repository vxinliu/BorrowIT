package com.example.borrowit.service.Impl;
import com.example.borrowit.Entity.*;
import com.example.borrowit.repository.*;
import com.example.borrowit.service.*;
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
    private DiscountRepository discountRepository;

    @Override
    public List<Commande> getAllCommandes() {
        return commandeRepository.findAll();
    }

    @Override
    public Commande getCommandeById(Long id) {
        return commandeRepository.findById(id).orElse(null);
    }

    @Override
    public Commande saveCommande(Commande commande) {
        return commandeRepository.save(commande);
    }
@Override
    public Commande updateCommande(Long id, Commande updatedCommande) {
        Commande existingCommande = commandeRepository.findById(id).orElseThrow();

        existingCommande.setTotalPrice(updatedCommande.getTotalPrice());
        existingCommande.setCreatedDate(updatedCommande.getCreatedDate());
        existingCommande.setStatus(updatedCommande.getStatus());
        existingCommande.setDescription(updatedCommande.getDescription());

        if (updatedCommande.getUser() != null && updatedCommande.getUser().getId() != null) {
            User user = userRepository.findById(updatedCommande.getUser().getId()).orElseThrow();
            existingCommande.setUser(user);
        }

        if (updatedCommande.getDiscount() != null && updatedCommande.getDiscount().getId() != null) {
            Discount discount = discountRepository.findById(updatedCommande.getDiscount().getId()).orElseThrow();
            existingCommande.setDiscount(discount);
        }

        return commandeRepository.save(existingCommande);
    }

    @Override
    public void deleteCommande(Long id) {
        commandeRepository.deleteById(id);
    }
    @Override
    public double calculateTotalPrice(Long commandeId) {
        Commande commande = getCommandeById(commandeId);
        if (commande != null && commande.getDiscount() != null) {
            double discountValue = commande.getTotalPrice() * (commande.getDiscount().getPercentage() / 100.0);
            return commande.getTotalPrice() - discountValue;
        }
        return commande != null ? commande.getTotalPrice() : 0.0;
    }

    @Override
    public Commande applyDiscountToCommande(Long commandeId, Long discountId) {
        Commande commande = getCommandeById(commandeId);
        Discount discount = discountRepository.findById(discountId).orElse(null);
        if (commande != null && discount != null) {
            commande.setDiscount(discount);
            return commandeRepository.save(commande);
        }
        return null;
    }

    //@Override
    //public List<Commande> getCommandesByUser(Long userId) {
   //     return commandeRepository.findByUserId(userId);
    //}

    @Override
    public List<Commande> getCommandesByDateRange(Date start, Date end) {
        return commandeRepository.findByCreatedDateBetween(start, end);
    }
}
