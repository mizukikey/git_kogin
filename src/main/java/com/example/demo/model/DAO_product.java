package com.example.demo.model;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DAO_product extends JpaRepository <Entity_product, Integer>{
	boolean existsByName(String name);

	Optional<Entity_product> findByName(String name);
}
