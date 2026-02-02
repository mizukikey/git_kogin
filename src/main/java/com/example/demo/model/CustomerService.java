package com.example.demo.model;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    @Autowired
    private DAO_customer customerRepository;

    public boolean isNameDuplicated(String name) {
        return customerRepository.existsByName(name);
    }

    public void save(Entity_customer customer) {
    	customerRepository.save(customer);
    }
    
    public boolean isNameDuplicatedForUpdate(String name, Integer id) {
        Optional<Entity_customer> opt = customerRepository.findByName(name);
        if (opt.isEmpty()) return false; // 名前なしは重複なし
        return id == null || !opt.get().getId().equals(id); // 新規 or 別IDなら重複
    }

}

