package com.example.demo.model;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    @Autowired
    private DAO_customer customerRepository;

    public boolean isUserIdDuplicated(String user_id) {
        return customerRepository.existsByUserId(user_id);
    }

    public void save(Entity_customer customer) {
    	customerRepository.save(customer);
    }
    
    public boolean isUserIdDuplicatedForUpdate(String user_id, Integer id) {
        Optional<Entity_customer> opt = customerRepository.findByUserId(user_id);
        if (opt.isEmpty()) return false;
        return id == null || !opt.get().getId().equals(id); // 新規 or 別IDなら重複
    }
    
    public List<Entity_customer> findAll() {
        return customerRepository.findAll();
    }
    
    public Entity_customer findById(Integer id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("顧客が見つかりません"));
    }


}

