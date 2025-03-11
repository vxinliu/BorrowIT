package com.example.borrowit.controller;

import com.example.borrowit.Entity.Contract;
import com.example.borrowit.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contracts")
public class ContractController {
    @Autowired
    private ContractService contractService;


    // Création d'un contrat
    @PostMapping

    public ResponseEntity<Contract> createContract(
            @RequestParam Long borrowerId,
            @RequestParam Long ownerId,
            @RequestBody Contract contract) {
        try {
            Contract createdContract = contractService.addContract(borrowerId, ownerId, contract);
            return new ResponseEntity<>(createdContract, HttpStatus.CREATED);
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
}
