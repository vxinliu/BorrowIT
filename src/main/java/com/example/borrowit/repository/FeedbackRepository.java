package com.example.borrowit.repository;

import com.example.borrowit.Entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback,Long> {
    List<Feedback> findByReported(boolean reported);

    @Query("SELECT f FROM Feedback f LEFT JOIN FETCH f.user")
    List<Feedback> findAllWithUser();

    List<Feedback> findByUserId(Long userId);
}
