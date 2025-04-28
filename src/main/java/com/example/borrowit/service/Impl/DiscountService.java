package com.example.borrowit.service.impl;

import com.example.borrowit.DTO.DiscountDTO;
import com.example.borrowit.Entity.Discount;
import com.example.borrowit.Entity.Item;
import com.example.borrowit.Entity.User;
import com.example.borrowit.repository.DiscountRepository;
import com.example.borrowit.repository.ItemRepository;
import com.example.borrowit.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DiscountService {
    @Autowired
    private final DiscountRepository discountRepository;
    @Autowired
    private final ItemRepository itemRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final Mailing mailing;

    public DiscountService(DiscountRepository discountRepository, ItemRepository itemRepository,
                           UserRepository userRepository, Mailing mailing) {
        this.discountRepository = discountRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.mailing = mailing;
    }


    // Create a new discount
    public Discount createDiscount(DiscountDTO discountDTO) {
        Discount discount = new Discount();
        discount.setName(discountDTO.getName());
        discount.setCode(discountDTO.getCode());
        discount.setPercentage(discountDTO.getPercentage());
        discount.setStartDate(discountDTO.getStartDate());
        discount.setEndDate(discountDTO.getEndDate());
        discount.setActive(discountDTO.isActive());
        // Récupérer l'item et l'associer
        Item item = itemRepository.findById(discountDTO.getItem_id())
                .orElseThrow(() -> new RuntimeException("Item not found with ID: " + discountDTO.getItem_id()));
        discount.setItem(item);

        Discount saved = discountRepository.save(discount);

// Envoyer un email après la création du discount
        String toEmail = "eyamhiir@gmail.com";  // Utiliser l'email de l'utilisateur (ou autre adresse)
        String subject = "Création d'un nouveau Discount";
        String text = "Un nouveau discount a été créé avec le code : " + discount.getCode() +
                " et un pourcentage de réduction de " + discount.getPercentage() + "%.";

        // Envoyer l'email
        mailing.sendDiscountNotification(toEmail, subject, text);

        /*List<User> allUsers = userRepository.findAll();
        for (User user : allUsers) {
            if (user.getEmail() != null && !user.getEmail().isEmpty()) {
                mailing.sendDiscountNotification(
                        user.getEmail(),
                        "🎁 Nouveau discount disponible !",
                        "Bonjour " + user.getName() + ",\n\nUn nouveau discount '" + saved.getName()
                                + "' est maintenant disponible jusqu’au " + saved.getEndDate() + " ! Profitez-en vite 😉" +
                                "Cordialement,\n" +
                                "L’équipe BorrowIt"
                );
            }
        }*/
        return saved;
    }
    @Scheduled(cron = "0 0 0 * * ?") // Tous les jours à minuit
    public void disableExpiredDiscounts() {
        LocalDate today = LocalDate.now();
        List<Discount> expiredDiscounts = discountRepository.findAll().stream()
                .filter(d -> d.getEndDate() != null && d.getEndDate().isBefore(today) && d.isActive())
                .toList();

        List<User> users = userRepository.findAll();

        for (Discount discount : expiredDiscounts) {
            discount.setActive(false);
            discountRepository.save(discount);

            for (User user : users) {
                if (user.getEmail() != null && !user.getEmail().isEmpty()) {
                    mailing.sendDiscountNotification(
                            user.getEmail(),
                            "⏰ Discount expiré : " + discount.getName(),
                            "Bonjour " + user.getName() + ",\n\nLe discount '" + discount.getName() + "' a expiré le "
                                    + discount.getEndDate() + ". Restez connecté pour les prochaines offres !" +
                                    "Cordialement,\n" +
                                    "L’équipe BorrowIt"
                    );
                }
            }
        }
    }

    // Get discount by ID
    public Discount getDiscountById(Long id) {
        return discountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Discount not found"));
    }

    // Get all discounts
    public List<Discount> getAllDiscounts() {
        return discountRepository.findAll();
    }

    // Update an existing discount
    public Discount updateDiscount(Long id, DiscountDTO discountDTO) {
        Discount discount = discountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Discount not found"));

        discount.setName(discountDTO.getName());
        discount.setCode(discountDTO.getCode());
        discount.setPercentage(discountDTO.getPercentage());
        discount.setStartDate(discountDTO.getStartDate());
        discount.setEndDate(discountDTO.getEndDate());
        discount.setActive(discountDTO.isActive());
        // Mettre à jour l'item si nécessaire
        Item item = itemRepository.findById(discountDTO.getItem_id())
                .orElseThrow(() -> new RuntimeException("Item not found with ID: " + discountDTO.getItem_id()));
        discount.setItem(item);
        return discountRepository.save(discount);
    }

    // Delete a discount by ID
    public void deleteDiscount(Long id) {
        discountRepository.deleteById(id);
    }


    public List<Discount> getDiscountsForItemAndActiveStatus(Long itemId, boolean active) {
        return discountRepository.findByItemIdAndActive(itemId, active);
    }
}
