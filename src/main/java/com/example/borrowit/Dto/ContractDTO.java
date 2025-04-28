package com.example.borrowit.Dto;

import com.example.borrowit.Entity.Contract;

public class ContractDTO {
    private Long id;
    private Long ownerId;
    private String ownerName;
    private Long borrowerId;
    private String borrowerName;
    public ContractDTO(Contract contract) {
        this.id = contract.getId();
        if (contract.getOwner() != null) {
            this.ownerId = contract.getOwner().getId();
            this.ownerName = contract.getOwner().getName() ;
        }
        if (contract.getBorrower() != null) {
            this.borrowerId = contract.getBorrower().getId();
            this.borrowerName = contract.getBorrower().getName() ;
        }
    }
}
