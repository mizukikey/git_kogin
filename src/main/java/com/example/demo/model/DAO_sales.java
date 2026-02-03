package com.example.demo.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DAO_sales extends JpaRepository <Entity_sales, Integer>{
    // 顧客IDで注文を取得
}
