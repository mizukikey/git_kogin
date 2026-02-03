package com.example.demo.model;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.SalesViewDto;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.SalesRepository;

@Service
public class SalesService {

    @Autowired
    private SalesRepository salesRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private ProductService productService;

//    public void save(Entity_sales sales) {
//        salesRepository.save(sales);
//    }
    
    public void validateStock(Integer productId, Integer quantity) {

    	Entity_product product = productService.findById(productId);

        if (quantity > product.getStock()) {
            throw new IllegalArgumentException(
                "在庫数（" + product.getStock() + "）を超えています"
            );
        }
    }
    
    
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
    

    @Transactional
    public void registerSale(SalesViewDto dto) {

        // 商品取得
        Entity_product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("商品が存在しません"));

        // 在庫チェック（念のため二重チェック）
        if (product.getStock() < dto.getQuantity()) {
            throw new IllegalArgumentException("在庫不足です");
        }

        // --- sales 登録 ---
        Entity_sales sales = new Entity_sales();
        sales.setProductId(dto.getProductId());
        sales.setCustomerId(dto.getCustomerId());
        sales.setManagerId(dto.getManagerId());
        sales.setQuantity(dto.getQuantity());
        sales.setSumPrice(product.getPrice() * dto.getQuantity());
        sales.setSalesDate(dto.getSalesDate());

        salesRepository.save(sales);

        // --- 在庫更新 ---
        product.setStock(product.getStock() - dto.getQuantity());
        productRepository.save(product);
    }
}
