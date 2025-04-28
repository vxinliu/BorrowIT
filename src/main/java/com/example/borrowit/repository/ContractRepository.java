package com.example.borrowit.repository;

import com.example.borrowit.Entity.Contract;
import com.example.borrowit.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ContractRepository extends JpaRepository<Contract, Long> {
    List<Contract> findByOwnerIdOrBorrowerId(Long ownerId, Long borrowerId);
    @Query("SELECT c FROM Contract c WHERE c.borrower.email = :email OR c.owner.email = :email")
    List<Contract> findByUserEmail(String email);
    @Query("SELECT c.borrower FROM Contract c WHERE c.id = :contractId")
    User findBorrowerByContractId(@Param("contractId") Long contractId);
    @Query("SELECT c.owner FROM Contract c WHERE c.id = :contractId")
    User findOwnerByContractId(@Param("contractId") Long contractId);
}
