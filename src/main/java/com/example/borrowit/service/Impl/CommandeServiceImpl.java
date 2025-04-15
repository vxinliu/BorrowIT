package com.example.borrowit.service.Impl;
import com.example.borrowit.DTO.CommandeDTO;
import com.example.borrowit.DTO.CreateCommandeRequest;
import com.example.borrowit.Entity.*;
import com.example.borrowit.repository.*;
import com.example.borrowit.service.*;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class CommandeServiceImpl implements CommandeService {
    /***********************************************

    @Autowired
    private ModelMapper modelMapper;

    public CommandeDTO convertToDto(Commande commande) {
        return modelMapper.map(commande, CommandeDTO.class);
    }

    public Commande convertToEntity(CommandeDTO dto) {
        return modelMapper.map(dto, Commande.class);
    }

     *
     *
     *
     *
     *
     */
    @Autowired
    private CommandeRepository commandeRepository;
    @Autowired
    private ItemRepository itemRepository;
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
        return commandeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Commande not found"));
    }

    @Override
    public Commande saveCommande(CreateCommandeRequest request) {
        Commande commande = new Commande();
        commande.setCreatedDate(new Date());
        commande.setStatus("EN_ATTENTE"); // par défaut
        commande.setDescription(request.getDescription());

        if (request.getUserId() != null) {
            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé"));
            commande.setUser(user);
        }

        if (request.getDiscountId() != null) {
            Discount discount = discountRepository.findById(request.getDiscountId())
                    .orElseThrow(() -> new EntityNotFoundException("Remise non trouvée"));
            commande.setDiscount(discount);
        }

        List<CommandeItem> commandeItems = new ArrayList<>();
        double total = 0;

        for (CreateCommandeRequest.ItemQuantity itemQuantity : request.getItems()) {
            Item item = itemRepository.findById(itemQuantity.getItemId())
                    .orElseThrow(() -> new EntityNotFoundException("Item non trouvé"));

            CommandeItem commandeItem = new CommandeItem();
            commandeItem.setItem(item);
            commandeItem.setQuantity(itemQuantity.getQuantity());
            commandeItem.setUnitPrice(item.getPrice());
            commandeItem.setCommande(commande);

            total += item.getPrice() * itemQuantity.getQuantity();
            commandeItems.add(commandeItem);
        }

        commande.setTotalPrice(total);
        commande.setCommandeItems(commandeItems);

        return commandeRepository.save(commande);
    }

    @Override
    public Commande updateCommande(Long id, Commande updatedCommande) {
        Commande existingCommande = commandeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Commande not found"));

        // Mise à jour des propriétés
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
        double total = 0;
        for (CommandeItem item : commande.getCommandeItems()) {
            total += item.getUnitPrice() * item.getQuantity();
        }

        if (commande.getDiscount() != null) {
            double discountValue = total * (commande.getDiscount().getPercentage() / 100.0);
            total -= discountValue;
        }

        commande.setTotalPrice(total);
        commandeRepository.save(commande); // Update en DB
        return total;
    }

    @Override
    public Commande applyDiscountToCommande(Long commandeId, Long discountId) {
        Commande commande = getCommandeById(commandeId);
        Discount discount = discountRepository.findById(discountId).orElseThrow(() -> new EntityNotFoundException("Discount not found"));

        commande.setDiscount(discount);
        return commandeRepository.save(commande);
    }

    @Override
    public List<Commande> getCommandesByDateRange(Date start, Date end) {
        return commandeRepository.findByCreatedDateBetween(start, end);
    }
}
