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

//    public void save(Entity_sales sales) {
//        salesRepository.save(sales);
//    }
    

    public void saveFromDto(SalesViewDto dto) {

        Entity_product product = productService.findById(dto.getProductId());

        int sumPrice = product.getPrice() * dto.getQuantity();

        Entity_sales sales = new Entity_sales();
        sales.setProductId(dto.getProductId());
        sales.setCustomerId(dto.getCustomerId());
        sales.setManagerId(dto.getManagerId());
        sales.setQuantity(dto.getQuantity());
        sales.setSumPrice(sumPrice);
        sales.setSalesDate(dto.getSalesDate());
        
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
