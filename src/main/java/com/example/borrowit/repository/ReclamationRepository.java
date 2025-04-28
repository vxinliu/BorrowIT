package com.example.borrowit.Repository;

import com.example.borrowit.Entity.Reclamation;
import com.example.borrowit.Entity.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReclamationRepository extends JpaRepository<Reclamation, Long>, JpaSpecificationExecutor<Reclamation> {

    // Find all Reclamations with sorting
    List<Reclamation> findAll(Sort sort);

    List<Reclamation> findByDeliveryId(Long deliveryId);

    List<Reclamation> findReclamationByUser(User user);
}
