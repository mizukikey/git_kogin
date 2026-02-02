package com.example.demo.model;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DAO_manager extends JpaRepository <Entity_manager, Integer>{
	boolean existsByUserId(String userId);
	Optional<Entity_manager> findByUserId(String userId);
}
