package com.example.borrowit.repository;

import com.example.borrowit.Entity.Reacts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReactsRepository extends JpaRepository<Reacts,Long> {
     List<Reacts> findReactsByFeedbackId(Long feedbackId);
     // Optional: If you need to fetch reacts with user data in one query
     // Method to fetch reacts with user data in a single query
     @Query("SELECT r FROM Reacts r " +
             "LEFT JOIN FETCH r.user " +
             "LEFT JOIN FETCH r.feedback " +
             "WHERE r.feedback.id = :feedbackId")
     List<Reacts> findByFeedbackIdWithUser(@Param("feedbackId") Long feedbackId);
     @Modifying
     @Query("DELETE FROM Reacts r WHERE r.feedback.id = :feedbackId AND r.user.id = :userId")
     void deleteByFeedbackIdAndUserId(@Param("feedbackId") Long feedbackId, @Param("userId") Long userId);

     Optional<Reacts> findByFeedbackIdAndUserId(Long feedbackId, Long userId);


}


