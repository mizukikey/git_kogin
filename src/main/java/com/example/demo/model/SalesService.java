package com.example.demo.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.SalesViewDto;
import com.example.demo.repository.SalesRepository;

@Service
public class SalesService {

    @Autowired
    private SalesRepository salesRepository;
    
    @Autowired
    private ProductService productService;

    public void save(Entity_sales sales) {
        salesRepository.save(sales);
    }

    // 確認画面用 DTO 変換
    public SalesViewDto toViewDto(Entity_sales sales) {
        SalesViewDto dto = new SalesViewDto();

        dto.setProductId(sales.getProductId());
        dto.setCustomerId(sales.getCustomerId());
        dto.setManagerId(sales.getManagerId());
        dto.setQuantity(sales.getQuantity());
        dto.setSumPrice(sales.getSumPrice());
        dto.setSalesDate(sales.getSalesDate());

        // 名前取得（Service or Repository で）
        dto.setProductName(
            productService.findById(sales.getProductId()).getName()
        );

        return dto;
    }
}
