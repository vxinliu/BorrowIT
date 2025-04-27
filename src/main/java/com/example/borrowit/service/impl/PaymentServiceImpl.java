package com.example.borrowit.service.impl;

import com.example.borrowit.Entity.Commande;
import com.example.borrowit.Entity.Contract;
import com.example.borrowit.Entity.Payment;
import com.example.borrowit.repository.ContractRepository;
import com.example.borrowit.repository.PaymentRepository;
import com.example.borrowit.service.PaymentService;

import com.stripe.model.PaymentIntent;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.File;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;




@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ContractRepository contractRepository;

    @Override
    public Payment addPayment(Long contractId, Payment payment) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new RuntimeException("Contrat non trouvé avec ID: " + contractId));

        payment.setContract(contract);
        payment.setDate(new Date()); // Définit la date actuelle
        payment.setStatus(Payment.PaymentStatus.PENDING); // Par défaut
        return paymentRepository.save(payment);
    }

    // Méthode pour créer un paiement et l'ajouter dans la base de données
    public Payment createPaymentFromStripe(Long contractId, PaymentIntent intent) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new IllegalArgumentException("Contrat non trouvé"));

        Payment payment = new Payment();
        payment.setContract(contract);
        payment.setAmount(intent.getAmount() / 100.0); // ✅ Utiliser le montant prévu
        payment.setStripePaymentIntentId(intent.getId());
        payment.setStatus(Payment.PaymentStatus.PENDING);
        payment.setDate(new Date());

        return paymentRepository.save(payment);  // Sauvegarde du paiement
    }


    // Méthode pour mettre à jour le statut du paiement
    public void updatePaymentStatus(String paymentIntentId, String status) {
        Payment payment = paymentRepository.findByStripePaymentIntentId(paymentIntentId)
                .orElseThrow(() -> new IllegalArgumentException("Paiement non trouvé"));

        if ("succeeded".equalsIgnoreCase(status)) {
            payment.setStatus(Payment.PaymentStatus.SUCCEEDED);
        } else if ("failed".equalsIgnoreCase(status)) {
            payment.setStatus(Payment.PaymentStatus.FAILED);
        }

        paymentRepository.save(payment);  // Mise à jour du statut
    }

    @Override
    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paiement non trouvé avec ID: " + id));
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    @Override
    public Payment updatePayment(Long id, Payment updatedPayment) {
        Payment existing = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paiement non trouvé avec ID: " + id));

        existing.setAmount(updatedPayment.getAmount());
        existing.setDate(updatedPayment.getDate());
        existing.setStatus(updatedPayment.getStatus());
        return paymentRepository.save(existing);
    }

    @Override
    public void deletePayment(Long id) {
        if (!paymentRepository.existsById(id)) {
            throw new RuntimeException("Aucun paiement trouvé avec ID: " + id);
        }
        paymentRepository.deleteById(id);
    }
    public Payment getPaymentByContractId(Long contractId) {
        return paymentRepository.findByContractId(contractId)
                .orElseThrow(() -> new RuntimeException("Aucun paiement trouvé pour le contrat avec ID: " + contractId));
    }


}
