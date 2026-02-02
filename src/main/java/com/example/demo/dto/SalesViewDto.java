package com.example.demo.dto;

import java.sql.Date;

public class SalesViewDto {

    private Integer id;
    private String productName;
    private Integer quantity;
    private Integer sumPrice;
    private String customerName;
    private String managerName;
    private Date salesDate;

    // ★ JPQL new 用コンストラクタ（完全一致）
    public SalesViewDto(
            Integer id,
            String productName,
            Integer quantity,
            Integer sumPrice,
            String customerName,
            String managerName,
            Date salesDate
    ) {
        this.id = id;
        this.productName = productName;
        this.quantity = quantity;
        this.sumPrice = sumPrice;
        this.customerName = customerName;
        this.managerName = managerName;
        this.salesDate = salesDate;
    }

    /* ===== getter（setter不要） ===== */

    public Integer getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Integer getSumPrice() {
        return sumPrice;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getManagerName() {
        return managerName;
    }

    public Date getSalesDate() {
        return salesDate;
    }
}
