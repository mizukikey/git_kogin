package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.dto.SalesViewDto;
import com.example.demo.model.Entity_sales;

@Repository
public interface SalesRepository extends JpaRepository<Entity_sales, Integer> {

    @Query("""
        SELECT new com.example.demo.dto.SalesViewDto(
            s.id,
            p.name,
            s.quantity,
            s.sumPrice,
            c.name,
            m.name,
            s.salesDate
        )
        FROM Entity_sales s
        JOIN Entity_product p ON s.productId = p.id
        JOIN Entity_customer c ON s.customerId = c.id
        JOIN Entity_manager m ON s.managerId = m.id
        ORDER BY s.id ASC
    """)
    List<SalesViewDto> findSalesViewList();
}
