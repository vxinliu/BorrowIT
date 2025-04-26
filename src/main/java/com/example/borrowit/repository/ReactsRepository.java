package com.example.borrowit.repository;

import com.example.borrowit.Entity.Reacts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReactsRepository extends JpaRepository<Reacts,Long> {
     List<Reacts> findReactsByFeedbackId(Long feedbackId);
     // Optional: If you need to fetch reacts with user data in one query
     // Method to fetch reacts with user data in a single query
     @Query("SELECT r FROM Reacts r LEFT JOIN FETCH r.user WHERE r.feedback.id = :feedbackId")
     List<Reacts> findByFeedbackIdWithUser(@Param("feedbackId") Long feedbackId);
}


