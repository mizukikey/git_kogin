package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.dto.SalesViewDto;
import com.example.demo.model.Entity_sales;

@Repository
public interface SalesRepository extends JpaRepository<Entity_sales, Integer> {
    
	@Query("""
		    SELECT new com.example.demo.dto.SalesViewDto(
		        s.id,
		        s.product.name,
		        s.quantity,
		        s.sumPrice,
		        s.customer.name,
		        s.manager.name,
		        s.salesDate
		    )
		    FROM Entity_sales s
		    ORDER BY s.id ASC
		""")
		List<SalesViewDto> findSalesViewList();

    
    
    // ★ delete_confirm 用（ID指定）
	@Query("""
		    SELECT new com.example.demo.dto.SalesViewDto(
		        s.id,
		        s.product.id,
		        s.product.name,
		        s.quantity,
		        s.sumPrice,
		        s.customer.id,
		        s.customer.name,
		        s.manager.id,
		        s.manager.name,
		        s.salesDate
		    )
		    FROM Entity_sales s
		    WHERE s.id = :id
		""")
		SalesViewDto findSalesViewById(Integer id);
	
	@Query("""
		    SELECT new com.example.demo.dto.SalesViewDto(
		        s.id,
		        s.product.name,
		        s.quantity,
		        s.sumPrice,
		        s.customer.name,
		        s.manager.name,
		        s.salesDate
		    )
		    FROM Entity_sales s
		    WHERE s.customer.id = :customerId
		    ORDER BY s.id ASC
		""")
	 List<SalesViewDto> findByCustomerId(@Param("customerId") Integer customerId);
}
