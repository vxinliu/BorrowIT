package com.example.borrowit.service;

import com.example.borrowit.Entity.Reacts;


import java.util.List;

public interface IReactsService {
    public List<Reacts> retrieveAllReacts();
    public Reacts retrieveReacts(Long reactsId);
    public Reacts addReact(Reacts r);
    public void removeReact(Long reactId);
    public Reacts modifyReact(Reacts r);
    public long countReactionsForFeedback(Long feedbackId);
    public List<Reacts> getReactsForFeedback(Long feedbackId);

    // Here we will add later methods calling keywords and methods calling JPQL


    Reacts addOrUpdateReaction(Long feedbackId, Long userId, Reacts.Reaction reaction);
    void removeReaction(Long feedbackId, Long userId);
}
