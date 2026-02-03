package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Entity_manager;

@Repository
public interface ManagerRepository extends JpaRepository<Entity_manager, Integer> {
}

