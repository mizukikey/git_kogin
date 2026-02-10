package com.example.demo.model;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private DAO_product productRepository;
    
    @Autowired
    private DAO_sales salesRepository;

    public boolean isNameDuplicated(String name) {
        return productRepository.existsByName(name);
    }

    public void save(Entity_product product) {
        productRepository.save(product);
    }
    
    public boolean isNameDuplicatedForUpdate(String name, Integer id) {
        Optional<Entity_product> opt = productRepository.findByName(name);
        if (opt.isEmpty()) return false; // 名前なしは重複なし
        return id == null || !opt.get().getId().equals(id); // 新規 or 別IDなら重複
    }
    
    // ← ここ
    public List<Entity_product> findAll() {
        return productRepository.findAll();
    }
    
    public Entity_product findById(Integer id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("商品が見つかりません"));
    }
    
    public void deleteProduct(Integer productId) {

        // 売上が存在するか確認
        if (salesRepository.existsByProductId(productId)) {
            throw new IllegalStateException(
                "売上履歴が存在する商品は削除できません"
            );
        }

        productRepository.deleteById(productId);
    }


}

