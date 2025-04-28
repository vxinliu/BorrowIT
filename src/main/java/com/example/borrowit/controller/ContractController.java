package com.example.borrowit.controller;

import com.example.borrowit.Dto.ContractDTO;
import com.example.borrowit.Entity.Contract;
import com.example.borrowit.Entity.User;
import com.example.borrowit.service.ContractService;
import com.example.borrowit.service.impl.ContractServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contracts")


public class ContractController {
    @Autowired
    private ContractServiceImpl contractService;


    // Création d'un contrat
    @PostMapping
    public ResponseEntity<Contract> createContract(
            @RequestParam Long borrowerId,
            @RequestParam Long ownerId,
            @RequestParam Long commandeId,
            @RequestBody Contract contract) {

        System.out.println("borrowerId: " + borrowerId + ", ownerId: " + ownerId + ", commandeId: " + commandeId);
        System.out.println("Contract received: " + contract);

        try {
            Contract createdContract = contractService.addContract(borrowerId, ownerId, commandeId, contract);
            System.out.println("Contract created with ID: " + createdContract.getId());  // Log ID
            return new ResponseEntity<>(createdContract, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace(); // Affiche l'erreur complète
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update-signatures/{contractId}")
    public Contract updateSignatures(@PathVariable Long contractId, @RequestBody Contract contract) {
        return contractService.updateSignatures(contractId, contract);
    }
    @PutMapping("/update-Borrowersignature/{contractId}")
    public Contract updateBorrowerSignature(@PathVariable Long contractId, @RequestBody Contract contract) {
        return contractService.updateBorrowerSignature(contractId, contract);
    }
    // Enregistrer les signatures
    @PutMapping("/{id}/signatures")
    public ResponseEntity<Contract> saveSignatures(
            @PathVariable Long id,
            @RequestParam String ownerSignature,
            @RequestParam String borrowerSignature) {
        try {
            Contract contract = contractService.saveSignatures(id, ownerSignature, borrowerSignature);
            return new ResponseEntity<>(contract, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Récupérer tous les contrats
    @GetMapping
    public ResponseEntity<List<Contract>> getAllContracts() {
        List<Contract> contracts = contractService.getAllContracts();
        return new ResponseEntity<>(contracts, HttpStatus.OK);
    }

    // Récupérer un contrat par ID
    @GetMapping("/{id}")
    public ResponseEntity<Contract> getContractById(@PathVariable Long id) {
        Contract contract = contractService.getContractById(id);
        if (contract != null) {
            return new ResponseEntity<>(contract, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Mettre à jour un contrat
    @PutMapping("/{id}")
    public ResponseEntity<Contract> updateContract(@PathVariable Long id, @RequestBody Contract contract) {
        Contract updatedContract = contractService.updateContract(id, contract);
        if (updatedContract != null) {
            return new ResponseEntity<>(updatedContract, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Supprimer un contrat
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContract(@PathVariable Long id) {
        try {
            contractService.deleteContract(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/user/{email}")
    public ResponseEntity<List<Contract>> getContractsByUserEmail(@PathVariable String email) {
        List<Contract> contracts = contractService.getContractsByUserEmail(email);
        return ResponseEntity.ok(contracts);
    }
@GetMapping("/borrower-by-contract/{contractId}")
    public ResponseEntity<User>getBorrowerByContractId(@PathVariable Long contractId) {
    User borrower = contractService.getBorrowerByContractId(contractId);
    if (borrower != null) {
        return new ResponseEntity<>(borrower, HttpStatus.OK);
    } else {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
    @GetMapping("/owner-by-contract/{contractId}")
    public ResponseEntity<User>getOwneryContractId(@PathVariable Long contractId) {
        User owner = contractService.getOwneryContractId(contractId);
        if (owner != null) {
            return new ResponseEntity<>(owner, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}
