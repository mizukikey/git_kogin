package com.example.demo.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

public class SalesViewDto {

    private Integer id;
    @NotNull(message = "商品を選択してください")
    private Integer productId;
    
    private String productName;
    
    @NotNull(message = "数量を入力してください")
    @Min(value = 1, message = "数量は1以上で入力してください")
    private Integer quantity;
    
//    @NotNull(message = "合計金額を入力してください")
//    @Min(value = 1, message = "合計金額は1円以上で入力してください")
    private Integer sumPrice;
    
//    @NotNull(message = "顧客を選択してください")
    private Integer customerId;
    private String customerName;
    
    @NotNull(message = "担当者を選択してください")
    private Integer managerId;
    private String managerName;
    
//    @NotNull(message = "販売日を入力してください")
//    private Date salesDate;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "販売日を入力してください")
    private LocalDate salesDate;

    
    public SalesViewDto() {}
    // ★ JPQL new 用コンストラクタ（完全一致）
    public SalesViewDto(
            Integer id,
            String productName,
            Integer quantity,
            Integer sumPrice,
            String customerName,
            String managerName,
            LocalDate salesDate
    ) {
        this.id = id;
        this.productName = productName;
        this.quantity = quantity;
        this.sumPrice = sumPrice;
        this.customerName = customerName;
        this.managerName = managerName;
        this.salesDate = salesDate;
    }
    
    public SalesViewDto(
    	    Integer id,
    	    Integer productId,
    	    String productName,
    	    Integer quantity,
    	    Integer sumPrice,
    	    Integer customerId,
    	    String customerName,
    	    Integer managerId,
    	    String managerName,
    	    LocalDate salesDate
    	) {
    	    this.id = id;
    	    this.productId = productId;
    	    this.productName = productName;
    	    this.quantity = quantity;
    	    this.sumPrice = sumPrice;
    	    this.customerId = customerId;
    	    this.customerName = customerName;
    	    this.managerId = managerId;
    	    this.managerName = managerName;
    	    this.salesDate = salesDate;
    	}

    /* ===== getter（setter不要） ===== */

    

    public LocalDate getSalesDate() {
        return salesDate;
    }
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
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
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getManagerName() {
		return managerName;
	}
	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}
	public void setSalesDate(LocalDate salesDate) {
		this.salesDate = salesDate;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	public Integer getManagerId() {
		return managerId;
	}
	public void setManagerId(Integer managerId) {
		this.managerId = managerId;
	}
}
