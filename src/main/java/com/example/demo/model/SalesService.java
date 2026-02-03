package com.example.demo.model;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.SalesViewDto;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.ManagerRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.SalesRepository;

@Service
public class SalesService {

    @Autowired
    private SalesRepository salesRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private ManagerRepository managerRepository;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private ManagerService managerService;

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
    
    
//    public void saveFromDto(SalesViewDto dto) {
//
//        Entity_product product = productService.findById(dto.getProductId());
//
//        int sumPrice = product.getPrice() * dto.getQuantity();
//
//        Entity_sales sales = new Entity_sales();
//        sales.setProductId(dto.getProductId());
//        sales.setCustomerId(dto.getCustomerId());
//        sales.setManagerId(dto.getManagerId());
//        sales.setQuantity(dto.getQuantity());
//        sales.setSumPrice(sumPrice);
//        sales.setSalesDate(dto.getSalesDate());
//        
//        salesRepository.save(sales);
//    }
    
    @Transactional
    public void saveFromDto(SalesViewDto dto) {

        // 関連Entity取得
        Entity_product product = productService.findById(dto.getProductId());
        Entity_customer customer = customerService.findById(dto.getCustomerId());
        Entity_manager manager = managerService.findById(dto.getManagerId());

        // 合計金額計算
        int sumPrice = product.getPrice() * dto.getQuantity();

        // sales 作成
        Entity_sales sales = new Entity_sales();
        sales.setProduct(product);
        sales.setCustomer(customer);
        sales.setManager(manager);
        sales.setQuantity(dto.getQuantity());
        sales.setSumPrice(sumPrice);
        sales.setSalesDate(dto.getSalesDate());

        salesRepository.save(sales);

        // 在庫更新（登録時）
        product.setStock(product.getStock() - dto.getQuantity());
    }


    // 確認画面用 DTO 変換
//    public SalesViewDto toViewDto(Entity_sales sales) {
//        SalesViewDto dto = new SalesViewDto();
//
//        dto.setProductId(sales.getProductId());
//        dto.setCustomerId(sales.getCustomerId());
//        dto.setManagerId(sales.getManagerId());
//        dto.setQuantity(sales.getQuantity());
//        dto.setSumPrice(sales.getSumPrice());
//        dto.setSalesDate(sales.getSalesDate());
//
//        // 名前取得（Service or Repository で）
//        dto.setProductName(
//            productService.findById(sales.getProductId()).getName()
//        );
//
//        return dto;
//    }
    
    public SalesViewDto toViewDto(Entity_sales sales) {
        SalesViewDto dto = new SalesViewDto();

        dto.setId(sales.getId());
        dto.setProductId(sales.getProduct().getId());
        dto.setProductName(sales.getProduct().getName());
        dto.setCustomerId(sales.getCustomer().getId());
        dto.setCustomerName(sales.getCustomer().getName());
        dto.setManagerId(sales.getManager().getId());
        dto.setManagerName(sales.getManager().getName());
        dto.setQuantity(sales.getQuantity());
        dto.setSumPrice(sales.getSumPrice());
        dto.setSalesDate(sales.getSalesDate());

        return dto;
    }

    

//    @Transactional
//    public void registerSale(SalesViewDto dto) {
//
//        // 商品取得
//        Entity_product product = productRepository.findById(dto.getProductId())
//                .orElseThrow(() -> new IllegalArgumentException("商品が存在しません"));
//
//        // 在庫チェック（念のため二重チェック）
//        if (product.getStock() < dto.getQuantity()) {
//            throw new IllegalArgumentException("在庫不足です");
//        }
//
//        // --- sales 登録 ---
//        Entity_sales sales = new Entity_sales();
//        sales.setProductId(dto.getProductId());
//        sales.setCustomerId(dto.getCustomerId());
//        sales.setManagerId(dto.getManagerId());
//        sales.setQuantity(dto.getQuantity());
//        sales.setSumPrice(product.getPrice() * dto.getQuantity());
//        sales.setSalesDate(dto.getSalesDate());
//
//        salesRepository.save(sales);
//
//        // --- 在庫更新 ---
//        product.setStock(product.getStock() - dto.getQuantity());
//        productRepository.save(product);
//    }
    
    @Transactional
    public void registerSale(SalesViewDto dto) {

        Entity_product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("商品が存在しません"));

        if (product.getStock() < dto.getQuantity()) {
            throw new IllegalArgumentException("在庫不足です");
        }

        Entity_customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow();

        Entity_manager manager = managerRepository.findById(dto.getManagerId())
                .orElseThrow();

        Entity_sales sales = new Entity_sales();
        sales.setProduct(product);
        sales.setCustomer(customer);
        sales.setManager(manager);
        sales.setQuantity(dto.getQuantity());
        sales.setSumPrice(product.getPrice() * dto.getQuantity());
        sales.setSalesDate(dto.getSalesDate());

        salesRepository.save(sales);

        // 在庫減算
        product.setStock(product.getStock() - dto.getQuantity());
    }

    
    public SalesViewDto findByIdForUpdate(Integer id) {
        Entity_sales sales = salesRepository.findById(id)
                .orElseThrow();

        SalesViewDto dto = new SalesViewDto();
        dto.setId(sales.getId());
        dto.setProductId(sales.getProduct().getId());
        dto.setCustomerId(sales.getCustomer().getId());
        dto.setManagerId(sales.getManager().getId());
        dto.setQuantity(sales.getQuantity());
        dto.setSalesDate(sales.getSalesDate());

        return dto;
    }

    
    @Transactional
    public void update(SalesViewDto dto) {

        Entity_sales sales = salesRepository.findById(dto.getId())
                .orElseThrow();

        int oldQty = sales.getQuantity();
        int newQty = dto.getQuantity();

        if (!sales.getProduct().getId().equals(dto.getProductId())) {
            // 商品変更時
            sales.getProduct().setStock(
                sales.getProduct().getStock() + oldQty);

            Entity_product newProduct = productRepository.findById(dto.getProductId())
                    .orElseThrow();

            newProduct.setStock(newProduct.getStock() - newQty);
            sales.setProduct(newProduct);
        } else {
            // 数量変更のみ
            int diff = newQty - oldQty;
            sales.getProduct().setStock(
                sales.getProduct().getStock() - diff);
        }

        sales.setQuantity(newQty);
        sales.setSalesDate(dto.getSalesDate());
        sales.setCustomer(customerRepository.findById(dto.getCustomerId()).orElseThrow());
        sales.setManager(managerRepository.findById(dto.getManagerId()).orElseThrow());
    }
    
    @Transactional
    public void deleteSale(Integer salesId) {

        Entity_sales sales = salesRepository.findById(salesId)
                .orElseThrow(() -> new IllegalArgumentException("売上が存在しません"));

        Entity_product product = sales.getProduct();

        // 在庫を戻す
        product.setStock(
            product.getStock() + sales.getQuantity()
        );

        // 売上削除
        salesRepository.delete(sales);
    }


}
