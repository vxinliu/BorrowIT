package com.example.borrowit.controller;

import com.example.borrowit.Entity.Reacts;
import com.example.borrowit.service.ReactsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reacts")
public class ReactsController {

    @Autowired
    private ReactsService reactsService;

    @GetMapping
    public List<Reacts> getAllReacts() {
        return reactsService.retrieveAllReacts();
    }

    @GetMapping("/{id}")
    public Reacts getReactById(@PathVariable Long id) {
        return reactsService.retrieveReact(id);
    }

    @PostMapping("/add-react")
    public Reacts createReact(@RequestBody Reacts react) {
        return reactsService.addReact(react);
    }

    @PutMapping("/modify-react")
    public Reacts updateReact(@RequestBody Reacts react) {
        return reactsService.modifyReact(react);
    }

    @DeleteMapping("/{id}")
    public void deleteReact(@PathVariable Long id) {
        reactsService.removeReact(id);
    }
}
