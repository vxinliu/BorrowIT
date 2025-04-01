package com.example.borrowit.service;

import com.example.borrowit.Entity.Reacts;

import java.util.List;

public interface ReactsService {
    List<Reacts> retrieveAllReacts();
    Reacts retrieveReact(Long reactId);
    Reacts addReact(Reacts react);
    void removeReact(Long reactId);
    Reacts modifyReact(Reacts react);
}
