package com.example.demo.model;



import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "sales")
public class Entity_sales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

//    @Column(name = "product_id", nullable = false)
//    @NotNull(message = "商品IDは必須です")
//    private Integer productId;
    
    // 商品
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Entity_product product;

    @NotNull(message = "数量は必須です")
    @Min(value = 1, message = "数量は1以上で入力してください")
    private Integer quantity;

    @Column(name = "sum_price", nullable = false)
    @NotNull(message = "合計金額は必須です")
    @Min(value = 0, message = "合計金額は0以上にしてください")
    private Integer sumPrice;

//    @Column(name = "customer_id", nullable = false)
//    @NotNull(message = "顧客IDは必須です")
//    private Integer customerId;
    
    // 顧客
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Entity_customer customer;

//    @Column(name = "manager_id", nullable = false)
//    @NotNull(message = "管理者IDは必須です")
//    private Integer managerId;
    
    // 担当者
    @ManyToOne
    @JoinColumn(name = "manager_id", nullable = false)
    private Entity_manager manager;

    @Column(name = "sales_date", nullable = false)
    @NotNull(message = "販売日は必須です")
    private LocalDate salesDate;

    /* ===== コンストラクタ ===== */

    // JPA用（必須）
    public Entity_sales() {}

    // 登録用コンストラクタ（idなし）
//    public Entity_sales(
//            Integer productId,
//            Integer quantity,
//            Integer sumPrice,
//            Integer customerId,
//            Integer managerId,
//            LocalDate salesDate) {
//
//        this.productId = productId;
//        this.quantity = quantity;
//        this.sumPrice = sumPrice;
//        this.customerId = customerId;
//        this.managerId = managerId;
//        this.salesDate = salesDate;
//    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	

//	public Integer getProductId() {
//		return productId;
//	}
//
//	public void setProductId(Integer productId) {
//		this.productId = productId;
//	}

	public Entity_product getProduct() {
		return product;
	}

	public void setProduct(Entity_product product) {
		this.product = product;
	}

	public Entity_customer getCustomer() {
		return customer;
	}

	public void setCustomer(Entity_customer customer) {
		this.customer = customer;
	}

	public Entity_manager getManager() {
		return manager;
	}

	public void setManager(Entity_manager manager) {
		this.manager = manager;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getSumPrice() {
		return sumPrice;
	}

	public void setSumPrice(Integer sumPrice) {
		this.sumPrice = sumPrice;
	}

//	public Integer getCustomerId() {
//		return customerId;
//	}
//
//	public void setCustomerId(Integer customerId) {
//		this.customerId = customerId;
//	}
//
//	public Integer getManagerId() {
//		return managerId;
//	}
//
//	public void setManagerId(Integer managerId) {
//		this.managerId = managerId;
//	}

	public LocalDate getSalesDate() {
		return salesDate;
	}

	public void setSalesDate(LocalDate salesDate) {
		this.salesDate = salesDate;
	}
    
    
}
