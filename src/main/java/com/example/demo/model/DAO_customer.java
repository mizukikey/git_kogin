package com.example.demo.model;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DAO_customer extends JpaRepository <Entity_customer, Integer>{
	boolean existsByUserId(String userId);

	Optional<Entity_customer> findByUserId(String userId);
}
