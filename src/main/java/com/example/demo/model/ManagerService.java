package com.example.demo.model;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ManagerService {

    @Autowired
    private DAO_manager managerRepository;

    public boolean isUserIdDuplicated(String user_id) {
        return managerRepository.existsByUserId(user_id);
    }

    public void save(Entity_manager manager) {
    	managerRepository.save(manager);
    }
    
    public boolean isUserIdDuplicatedForUpdate(String user_id, Integer id) {
        Optional<Entity_manager> opt = managerRepository.findByUserId(user_id);
        if (opt.isEmpty()) return false;
        return id == null || !opt.get().getId().equals(id); // 新規 or 別IDなら重複
    }
    
    public List<Entity_manager> findAll() {
        return managerRepository.findAll();
    }
    
    public Entity_manager findById(Integer id) {
        return managerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("管理者が見つかりません"));
    }


}

