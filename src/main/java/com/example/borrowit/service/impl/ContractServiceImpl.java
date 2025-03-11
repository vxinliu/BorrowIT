package com.example.borrowit.service.impl;

import com.example.borrowit.Entity.Contract;
import com.example.borrowit.Entity.Payment;
import com.example.borrowit.Entity.User;
import com.example.borrowit.repository.ContractRepository;
import com.example.borrowit.repository.PaymentRepository;
import com.example.borrowit.repository.UserRepository;
import com.example.borrowit.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContractServiceImpl implements ContractService {
    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Contract addContract(Long borrowerId, Long ownerId,Contract contract) {
        User borrower = userRepository.findById(borrowerId).orElseThrow(() -> new RuntimeException("Borrower not found"));
        User owner = userRepository.findById(ownerId).orElseThrow(() -> new RuntimeException("Owner not found"));
        contract.setBorrower(borrower);
        contract.setOwner(owner);
        Payment payment = new Payment();
        payment.setContract(contract);
        payment.setAmount(12222F);
        paymentRepository.save(payment);
        contract.setPayment(payment);
        return contractRepository.save(contract);
    }

    @Override
    public Contract getContractById(Long id) {
        return contractRepository.findById(id).get();
    }

    @Override
    public List<Contract> getAllContracts() {
        return contractRepository.findAll();
    }

    @Override
    public Contract updateContract(Long id, Contract contract) {
        if (contractRepository.existsById(id)) {
            contract.setId(id);
            return contractRepository.save(contract);
        } else {
            throw new RuntimeException("Contract not found with id " + id);
        }
    }

    @Override
    public void deleteContract(Long id) {
        if (contractRepository.existsById(id)) {
            contractRepository.deleteById(id);
        } else {
            throw new RuntimeException("Contract not found with id " + id);
        }
    }
}
