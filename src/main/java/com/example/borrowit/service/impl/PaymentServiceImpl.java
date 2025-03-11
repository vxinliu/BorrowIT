package com.example.borrowit.service.impl;

import com.example.borrowit.Entity.Payment;
import com.example.borrowit.repository.PaymentRepository;
import com.example.borrowit.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;
//    @Override
//    public Payment addPayment(Payment payment) {
//        return paymentRepository.save(payment);
//    }

    @Override
    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id).get();
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    @Override
    public Payment updatePayment(Long id, Payment payment) {
        if (paymentRepository.existsById(id)) {
            payment.setId(id);
            return paymentRepository.save(payment);
        } else {
            throw new RuntimeException("Payment not found with id " + id);
        }
    }

    @Override
    public void deletePayment(Long id) {
        if (paymentRepository.existsById(id)) {
            paymentRepository.deleteById(id);
        } else {
            throw new RuntimeException("Payment not found with id " + id);
        }
    }
}
