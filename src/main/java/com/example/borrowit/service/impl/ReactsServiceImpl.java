package com.example.borrowit.service.impl;

import com.example.borrowit.Entity.Reacts;
import com.example.borrowit.repository.ReactsRepository;
import com.example.borrowit.service.IReactsService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReactsServiceImpl implements IReactsService {
    @Autowired
    private ReactsRepository reactsRepository;
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






}
