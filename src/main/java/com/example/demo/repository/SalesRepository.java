package com.example.demo.repository;

import java.time.LocalDate;
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
		      AND s.customer.id = :customerId
		""")
		SalesViewDto findByIdForCustomer(
		    @Param("id") Integer id,
		    @Param("customerId") Integer customerId);
	
	@Query("""
		    SELECT SUM(s.sumPrice)
		    FROM Entity_sales s
		    WHERE s.salesDate BETWEEN :from AND :to
		""")
		Integer sumTotalSales(
		    @Param("from") LocalDate from,
		    @Param("to") LocalDate to
		);

	
	@Query("""
			SELECT s.customer.name, SUM(s.sumPrice)
			FROM Entity_sales  s
			WHERE s.salesDate BETWEEN :from AND :to
			GROUP BY s.customer.id
			""")
			List<Object[]> sumByCustomer(
			    @Param("from") LocalDate from,
			    @Param("to") LocalDate to
			);
			
	@Query("""
			SELECT s.manager.name, SUM(s.sumPrice)
			FROM Entity_sales  s
			WHERE s.salesDate BETWEEN :from AND :to
			GROUP BY s.manager.id
			""")
			List<Object[]> sumByManager(
			    @Param("from") LocalDate from,
			    @Param("to") LocalDate to
			);
			
	@Query("""
			SELECT s.product.name, SUM(s.sumPrice)
			FROM Entity_sales  s
			WHERE s.salesDate BETWEEN :from AND :to
			GROUP BY s.product.id
			""")
			List<Object[]> sumByProduct(
			    @Param("from") LocalDate from,
			    @Param("to") LocalDate to
			);
	
	@Query("""
		    SELECT s.salesDate, SUM(s.sumPrice)
		    FROM Entity_sales s
		    WHERE s.salesDate BETWEEN :from AND :to
		    GROUP BY s.salesDate
		    ORDER BY s.salesDate
		""")
		List<Object[]> findDailySales(
		        @Param("from") LocalDate from,
		        @Param("to") LocalDate to);




}
