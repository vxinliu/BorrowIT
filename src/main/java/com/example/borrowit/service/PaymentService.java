package com.example.borrowit.service;

import com.example.borrowit.Entity.Payment;

import java.util.List;

public interface PaymentService {
    public Payment addPayment(Long contractId,Payment payment);
    public Payment getPaymentById(Long id);
    public List<Payment> getAllPayments();
    public Payment updatePayment(Long id, Payment payment);
    public void deletePayment(Long id);
}
