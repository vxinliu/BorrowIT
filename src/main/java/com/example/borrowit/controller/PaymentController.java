package com.example.borrowit.controller;

import com.example.borrowit.Entity.Contract;
import com.example.borrowit.Entity.Payment;
import com.example.borrowit.Entity.User;
import com.example.borrowit.repository.ContractRepository;
import com.example.borrowit.service.ContractService;
import com.example.borrowit.service.PaymentService;
import com.example.borrowit.service.impl.EmailService;
import com.example.borrowit.service.impl.PaymentServiceImpl;
import com.example.borrowit.service.impl.PdfGenerationService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/payments")

public class PaymentController {

    @Autowired
    private PaymentServiceImpl paymentService;
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private PdfGenerationService pdfService;
    @Autowired
    private EmailService emailService;


    @PostMapping("/{contractId}")
    public Payment addPayment(@PathVariable Long contractId, @RequestBody Payment payment) {
        return paymentService.addPayment(contractId, payment);
    }

    // ✅ Obtenir un paiement par ID
    @GetMapping("/{id}")
    public Payment getPaymentById(@PathVariable Long id) {
        return paymentService.getPaymentById(id);
    }

    // ✅ Obtenir tous les paiements
    @GetMapping
    public List<Payment> getAllPayments() {
        return paymentService.getAllPayments();
    }

    // ✅ Modifier un paiement
    @PutMapping("/{id}")
    public Payment updatePayment(@PathVariable Long id, @RequestBody Payment payment) {
        return paymentService.updatePayment(id, payment);
    }

    // ✅ Supprimer un paiement
    @DeleteMapping("/{id}")
    public void deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
    }

    @PostMapping("/confirm")
    public ResponseEntity<?> confirmPayment(
            @RequestParam String paymentIntentId,
            @RequestParam String status,
            @RequestParam Long contractId) {

        try {
            // 1. Récupération du contrat
            Contract contract = contractRepository.findById(contractId)
                    .orElseThrow(() -> new RuntimeException("Contrat non trouvé"));

            // 2. Génération des PDF
            byte[] contractPdf = null;
            try {
                contractPdf = pdfService.generateContractPdf(
                        contract.getId(),
                        contract.getBorrower().getName(),
                        contract.getCommande().getTotalPrice());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            byte[] invoicePdf = pdfService.generateInvoicePdf(
                    contract);

            // 3. Envoi par email
            try {
                emailService.sendPaymentConfirmation(
                        contract.getBorrower().getEmail(),
                        contract.getBorrower().getName(),
                        contract.getId(),
                        contractPdf,
                        invoicePdf
                );
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }

            return ResponseEntity.ok().build();

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Erreur technique: " + e.getMessage()));
        }
    }
    @GetMapping("/by-contract/{contractId}")
    public Payment getPaymentByContractId(@PathVariable Long contractId) {
        return paymentService.getPaymentByContractId(contractId);
    }
}