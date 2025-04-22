package com.example.borrowit.service.impl;

import com.example.borrowit.Entity.Feedback;
import com.example.borrowit.Entity.User;
import com.example.borrowit.repository.FeedbackRepository;
import com.example.borrowit.repository.ReactsRepository;
import com.example.borrowit.repository.UserRepository;
import com.example.borrowit.service.IFeedbackService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FeedbackServiceImpl implements IFeedbackService {
    @Autowired
    private FeedbackRepository feedbackRepository;
    @Autowired
    private ReactsRepository reactsRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EntityManager entityManager;
    @Override
    @Transactional
    public List<Feedback> retrieveAllFeedbacks() {

         return feedbackRepository.findAll();
    }

    @Override
    public Feedback retrieveFeedback(Long feedbackId) {

        return feedbackRepository.findById(feedbackId).orElse(null);
    }

    @Override
    public Feedback addFeedback(Feedback f) {
        if (f.getUser() != null && f.getUser().getId() != null) {
            User user = userRepository.findById(f.getUser().getId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            f.setUser(user); // JPA va maintenant persister le lien
        }

        Feedback feedback = feedbackRepository.save(f);

        if (f.getReacts() != null) {
            f.getReacts().forEach(reacts -> {
                reacts.setFeedback(feedback);
                reactsRepository.save(reacts);
            });
        }

        return feedback;
    }

@Override
    public void removeFeedback(Long feedbackId) {
        Optional<Feedback> feedback = feedbackRepository.findById(feedbackId);
        if (feedback.isPresent()) {
            feedbackRepository.deleteById(feedbackId);
        } else {
            throw new RuntimeException("Feedback not found with ID: " + feedbackId);
        }
    }



    @Override
    public Feedback modifyFeedback(Feedback f) {
        if (feedbackRepository.existsById(f.getId())) {
            return feedbackRepository.save(f);
        } else {
            throw new RuntimeException("Feedback not found with ID: " + f.getId());
        }
    }

    @Override
    public List<Feedback> getMostReactedFeedbacks(int topN) {
        String jpql = "SELECT f FROM Feedback f " +
                "JOIN f.reacts r " +
                "GROUP BY f.id " +
                "ORDER BY COUNT(r) DESC";
        Query query = entityManager.createQuery(jpql);
        query.setMaxResults(topN);  // Limit the results to the top N feedbacks
        return query.getResultList();  // Return the list of feedbacks
    }

    @Override
    public void reportFeedback(Long feedbackId, String reason) {
        Optional<Feedback> feedbackOpt = feedbackRepository.findById(feedbackId);
        if (feedbackOpt.isPresent()) {
            Feedback feedback = feedbackOpt.get();
            feedback.setReported(true);  // Mettre à jour l'état du feedback en signalé
            feedback.setReason(reason);  // Ajouter la raison du signalement
            feedbackRepository.save(feedback);
        } else {
            throw new RuntimeException("Feedback not found with ID: " + feedbackId);
        }
    }


    @Override
    public List<Feedback> retrieveReportedFeedbacks() {
        return feedbackRepository.findByReported(true);
    }

    @Override
    public Feedback rejectFeedback(Long feedbackId) {
        Optional<Feedback> feedbackOpt = feedbackRepository.findById(feedbackId);
        if (feedbackOpt.isPresent()) {
            Feedback feedback = feedbackOpt.get();
            feedback.setReported(false);  // Set the reported status to false (or 0)
            feedback.setReason(null);  // Optionally, clear the reason for reporting
            return feedbackRepository.save(feedback);  // Save the updated feedback
        } else {
            throw new RuntimeException("Feedback not found with ID: " + feedbackId);
        }
    }


}