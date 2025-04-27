package com.example.borrowit.repository;

import com.example.borrowit.Entity.Contract;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContractRepository extends JpaRepository<Contract, Long> {
    List<Contract> findByOwnerIdOrBorrowerId(Long ownerId, Long borrowerId);

}
