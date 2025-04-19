package com.example.borrowit.repository;

import com.example.borrowit.Entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback,Long> {
    List<Feedback> findByReported(boolean reported);
}
