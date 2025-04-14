package com.example.borrowit.service;

import com.example.borrowit.Entity.Contract;

import java.util.List;

public interface ContractService {
    public Contract addContract(Long borrowerId, Long ownerId,Contract contract);

    // Méthode pour enregistrer les signatures
    Contract saveSignatures(Long contractId, String ownerSignature, String borrowerSignature);

    public Contract getContractById(Long id);
    public List<Contract> getAllContracts();
   public Contract updateContract(Long id, Contract contract);
   public void deleteContract(Long id);

    Contract updateSignatures(Long contractId, Contract contract);
}
