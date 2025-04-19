package com.example.borrowit.repository;

import com.example.borrowit.Entity.Reacts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReactsRepository extends JpaRepository<Reacts,Long> {

}
