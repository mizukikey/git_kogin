package com.example.demo.model;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DAO_customer extends JpaRepository <Entity_customer, Integer>{
	boolean existsByName(String name);

	Optional<Entity_customer> findByName(String name);
}
