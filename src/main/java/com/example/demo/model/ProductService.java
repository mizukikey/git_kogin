package com.example.demo.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private DAO_product productRepository;

    public boolean isNameDuplicated(String name) {
        return productRepository.existsByName(name);
    }

    public void save(Entity_product product) {
        productRepository.save(product);
    }
}

