package com.example.borrowit.controller;

import com.example.borrowit.Entity.Payment;
import com.example.borrowit.service.PaymentService;
import com.example.borrowit.service.impl.PaymentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")

public class PaymentController {

    @Autowired
    private PaymentService paymentService;


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
}