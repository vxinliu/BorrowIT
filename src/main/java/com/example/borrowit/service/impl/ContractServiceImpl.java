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
    public Contract addContract(Long borrowerId, Long ownerId, Contract contract) {
        User borrower = userRepository.findById(borrowerId).orElseThrow(() -> new RuntimeException("Borrower not found"));
        User owner = userRepository.findById(ownerId).orElseThrow(() -> new RuntimeException("Owner not found"));
        contract.setId(null);
        contract.setBorrower(borrower);
        contract.setOwner(owner);



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
        if (contractRepository.existsById(id)) {
            contract.setId(id);
            return contractRepository.save(contract);
        } else {
            throw new RuntimeException("Contract not found with id " + id);
        }
    }
    public Contract updateSignatures(Long contractId, Contract contract) {
        Contract existingContract = contractRepository.findById(contractId)
                .orElseThrow(() -> new RuntimeException("Contrat non trouvé"));

        // Mettre à jour les signatures
        existingContract.setOwnerSignature(contract.getOwnerSignature());
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
}
