package com.example.borrowit.service.impl;

import com.example.borrowit.Entity.Reacts;
import com.example.borrowit.repository.ReactsRepository;
import com.example.borrowit.service.IReactsService;
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
}
