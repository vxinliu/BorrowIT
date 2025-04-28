package com.example.borrowit.Repository;

import com.example.borrowit.Entity.Delivery;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long>, JpaSpecificationExecutor<Delivery> {

    // Find all Deliveries with sorting
    List<Delivery> findAll(Sort sort);
}
