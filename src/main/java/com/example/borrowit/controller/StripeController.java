package com.example.borrowit.controller;

import com.example.borrowit.Entity.Contract;
import com.example.borrowit.Entity.Payment;
import com.example.borrowit.repository.ContractRepository;
import com.example.borrowit.service.impl.PaymentServiceImpl;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/stripe")
public class StripeController {

    @Autowired
    private PaymentServiceImpl paymentService;
    @Autowired
    private ContractRepository contractRepository;

    @Value("${stripe.api.secret.key}")
    private String stripeSecretKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;
    }

    @PostMapping("/create-payment-intent")
    public ResponseEntity<?> createPaymentIntent(@RequestBody Map<String, Object> request) {
        try {
            Long contractId = ((Number) request.get("contractId")).longValue();
            double amount = ((Number) request.get("amount")).doubleValue();

            // Création de l'intention de paiement Stripe
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount((long)(amount * 100)) // Convertit en centimes
                    .setCurrency("eur")
                    .build();

            PaymentIntent intent = PaymentIntent.create(params);

            // Enregistrement du paiement dans la base de données
            paymentService.createPaymentFromStripe(contractId, intent);

            return ResponseEntity.ok(Map.of("clientSecret", intent.getClientSecret()));
        } catch (StripeException e) {
            return ResponseEntity.status(500).body(Map.of("error", "Erreur Stripe: " + e.getMessage()));
        }
    }

    @PostMapping("/confirm-payment")
    public ResponseEntity<?> confirmPayment(@RequestBody Map<String, String> request) {
        try {
            String paymentIntentId = request.get("paymentIntentId");
            String status = request.get("status");

            // Confirmation du paiement Stripe et mise à jour du statut dans la base de données
            paymentService.updatePaymentStatus(paymentIntentId, status);

            return ResponseEntity.ok(Map.of("message", "Payment status updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Erreur: " + e.getMessage()));
        }
    }

}
