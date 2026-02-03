package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Entity_customer;

@Repository
public interface CustomerRepository extends JpaRepository<Entity_customer, Integer> {
	Entity_customer findByUserIdAndPassword(String userId, String password);
}
