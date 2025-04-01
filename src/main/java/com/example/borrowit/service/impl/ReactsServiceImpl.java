package com.example.borrowit.service.impl;

import com.example.borrowit.Entity.Reacts;
import com.example.borrowit.repository.ReactsRepository;
import com.example.borrowit.service.ReactsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReactsServiceImpl implements ReactsService {
    @Autowired
    private ReactsRepository reactsRepository;

    @Override
    public List<Reacts> retrieveAllReacts() {
        return reactsRepository.findAll();
    }

    @Override
    public Reacts retrieveReact(Long reactId) {
        Optional<Reacts> react = reactsRepository.findById(reactId);
        return react.orElse(null);  // Retourne null si non trouvé
    }

    @Override
    public Reacts addReact(Reacts react) {
        return reactsRepository.save(react);
    }

    @Override
    public void removeReact(Long reactId) {
        reactsRepository.deleteById(reactId);
    }

    @Override
    public Reacts modifyReact(Reacts react) {
        return reactsRepository.save(react);
    }
}