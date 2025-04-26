package com.example.borrowit.service.impl;

import com.example.borrowit.Entity.Reacts;
import com.example.borrowit.repository.FeedbackRepository;
import com.example.borrowit.repository.ReactsRepository;
import com.example.borrowit.repository.UserRepository;
import com.example.borrowit.service.IReactsService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReactsServiceImpl implements IReactsService {
    @Autowired
    private ReactsRepository reactsRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FeedbackRepository feedbackRepository;
    @Autowired
    private EntityManager entityManager;
    @Override
    public List<Reacts> retrieveAllReacts() {
        return reactsRepository.findAll();
    }

    @Override
    public Reacts retrieveReacts(Long reactsId) {
        Optional<Reacts> react = reactsRepository.findById(reactsId);
        return react.orElse(null);  // Returns null if not found
    }

    @Override
    public Reacts addReact(Reacts r) {
        // Load the full user and feedback from DB
        Long userId = r.getUser().getId();
        Long feedbackId = r.getFeedback().getId();

        r.setUser(userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found")));
        r.setFeedback(feedbackRepository.findById(feedbackId).orElseThrow(() -> new RuntimeException("Feedback not found")));

        // Set current date if not provided
        if (r.getDate() == null) {
            r.setDate(new Date());
        }

        return reactsRepository.save(r);
    }

    @Override
    public void removeReact(Long reactId) {
        reactsRepository.deleteById(reactId);
    }

    @Override
    public Reacts modifyReact(Reacts r) {
        return reactsRepository.save(r);
    }

    @Override
    public long countReactionsForFeedback(Long feedbackId) {
        // Create the JPQL query to count reactions for the given feedback ID
        String jpql = "SELECT COUNT(r) FROM Reacts r WHERE r.feedback.id = :feedbackId";

        // Create a query using EntityManager
        Query query = entityManager.createQuery(jpql);

        // Set the feedbackId parameter
        query.setParameter("feedbackId", feedbackId);

        // Execute the query and return the count result as long
        return (long) query.getSingleResult();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reacts> getReactsForFeedback(Long feedbackId) {
        return reactsRepository.findByFeedbackIdWithUser(feedbackId);
    }


}
