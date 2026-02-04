package com.example.demo.model;

import java.time.LocalDate;

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
    
//    @Transactional
//    public void updateSale(SalesViewDto dto) {
//
//        // ① 更新対象を取得
//        Entity_sales sales = salesRepository
//            .findById(dto.getId())
//            .orElseThrow(() ->
//                new IllegalArgumentException("注文が存在しません"));
//
//        // ② 関連Entity取得
//        Entity_product product =
//            productRepository.findById(dto.getProductId())
//                .orElseThrow();
//
//        Entity_manager manager =
//            managerRepository.findById(dto.getManagerId())
//                .orElseThrow();
//
//        Entity_customer customer =
//            customerRepository.findById(dto.getCustomerId())
//                .orElseThrow();
//
//        // ③ 値を上書き
//        sales.setProduct(product);
//        sales.setQuantity(dto.getQuantity());
//        sales.setManager(manager);
//        sales.setCustomer(customer);
//        sales.setSalesDate(dto.getSalesDate());
//
//        int sumPrice =
//            product.getPrice() * dto.getQuantity();
//        sales.setSumPrice(sumPrice);
//
//        // ④ 保存（update）
//        salesRepository.save(sales);
//    }
    
    @Transactional
    public void updateSale(SalesViewDto dto) {

        Entity_sales sales =
            salesRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("注文が存在しません"));

        Entity_product oldProduct = sales.getProduct();

        Entity_product newProduct =
            productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("商品が存在しません"));

        int oldQuantity = sales.getQuantity();
        int newQuantity = dto.getQuantity();

        // 🔽 商品が同じ場合
        if (oldProduct.getId().equals(newProduct.getId())) {

            int diff = newQuantity - oldQuantity;

            if (newProduct.getStock() < diff) {
                throw new IllegalArgumentException("在庫が不足しています");
            }

            newProduct.setStock(newProduct.getStock() - diff);

        } else {
            // 🔽 商品が変わった場合

            // ① 旧商品の在庫を戻す
            oldProduct.setStock(oldProduct.getStock() + oldQuantity);

            // ② 新商品の在庫チェック
            if (newProduct.getStock() < newQuantity) {
                throw new IllegalArgumentException("在庫が不足しています");
            }

            // ③ 新商品の在庫を減らす
            newProduct.setStock(newProduct.getStock() - newQuantity);

            // ④ sales に新商品をセット
            sales.setProduct(newProduct);
        }

        // 共通更新
        sales.setQuantity(newQuantity);
        sales.setSalesDate(dto.getSalesDate());
        sales.setManager(
            managerRepository.findById(dto.getManagerId()).orElseThrow()
        );
        sales.setSumPrice(newProduct.getPrice() * newQuantity);
    }
    
    public Object getSummary(LocalDate from, LocalDate to, String type) {

        return switch (type) {
            case "total" -> salesRepository.sumTotalSales(from, to);
            case "customer" -> salesRepository.sumByCustomer(from, to);
            case "manager" -> salesRepository.sumByManager(from, to);
            case "product" -> salesRepository.sumByProduct(from, to);
            default -> null;
        };
    }




}
