package com.example.borrowit.Repository;

import com.example.borrowit.Entity.Delivery;
import com.example.borrowit.Entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    @Query("SELECT r.delivery FROM Request r WHERE r.borrower.id = :userId AND r.delivery IS NOT NULL")
    List<Delivery> findDeliveriesByUserId(@Param("userId") Long userId);
}
