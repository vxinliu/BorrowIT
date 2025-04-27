package com.example.borrowit.service.impl;

import com.example.borrowit.Entity.Commande;
import com.example.borrowit.Entity.Contract;
import com.example.borrowit.Entity.Payment;
import com.example.borrowit.Entity.User;
import com.example.borrowit.repository.CommandeRepository;
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
    @Autowired
    private CommandeRepository commandeRepository;

    @Override
    public Contract addContract(Long borrowerId, Long ownerId, Long commandeId, Contract contract) {
        User borrower = userRepository.findById(borrowerId).orElseThrow(() -> new RuntimeException("Borrower not found"));
        User owner = userRepository.findById(ownerId).orElseThrow(() -> new RuntimeException("Owner not found"));
        Commande commande = commandeRepository.findById(commandeId).orElseThrow(() -> new RuntimeException("Commande not found"));

        contract.setId(null);
        contract.setBorrower(borrower);
        contract.setOwner(owner);
        contract.setCommande(commande); // 🔥 Lien entre contrat et commande

        return contractRepository.save(contract);
    }


    // Méthode pour ajouter les signatures au contrat
    public Contract saveSignatures(Long contractId, String ownerSignature, String borrowerSignature) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new RuntimeException("Contract not found"));

        contract.setOwnerSignature(ownerSignature);
        contract.setBorrowerSignature(borrowerSignature);

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
        Contract existing = contractRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contract not found with id " + id));

        existing.setStartDate(contract.getStartDate());
        existing.setEndDate(contract.getEndDate());
        existing.setOwnerSignature(contract.getOwnerSignature());
        existing.setBorrowerSignature(contract.getBorrowerSignature());

        // MAJ owner si fourni
        if (contract.getOwner() != null && contract.getOwner().getId() != null) {
            User owner = userRepository.findById(contract.getOwner().getId())
                    .orElseThrow(() -> new RuntimeException("Owner not found"));
            existing.setOwner(owner);
        }

        // MAJ borrower si fourni
        if (contract.getBorrower() != null && contract.getBorrower().getId() != null) {
            User borrower = userRepository.findById(contract.getBorrower().getId())
                    .orElseThrow(() -> new RuntimeException("Borrower not found"));
            existing.setBorrower(borrower);
        }

        return contractRepository.save(existing);
    }

    public Contract updateSignatures(Long contractId, Contract contract) {
        Contract existingContract = contractRepository.findById(contractId)
                .orElseThrow(() -> new RuntimeException("Contrat non trouvé"));

        // Mettre à jour les signatures
        existingContract.setOwnerSignature(contract.getOwnerSignature());
        existingContract.setBorrowerSignature(contract.getBorrowerSignature());

        return contractRepository.save(existingContract);
    }
    public Contract updateBorrowerSignature(Long contractId, Contract contract) {
        Contract existingContract = contractRepository.findById(contractId)
                .orElseThrow(() -> new RuntimeException("Contrat non trouvé"));

        // Mettre à jour les signatures

        existingContract.setBorrowerSignature(contract.getBorrowerSignature());

        return contractRepository.save(existingContract);
    }
    @Override
    public void deleteContract(Long id) {
        if (contractRepository.existsById(id)) {
            contractRepository.deleteById(id);
        } else {
            throw new RuntimeException("Contract not found with id " + id);
        }
    }
    public List<Contract> getContractsByUserId(Long userId) {
        return contractRepository.findByOwnerIdOrBorrowerId(userId, userId);
    }
}
