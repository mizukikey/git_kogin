package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


@Entity
@Table(name="product")
public class Entity_product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(unique = true)
    @NotBlank(message = "名前は必須です")
    @Size(max = 100, message = "名前は100文字以内で入力してください")
    private String name;

    @NotNull(message = "値段は必須です")
    @Min(value = 0, message = "値段は0円以上にしてください")
    private Integer price;

    @NotNull(message = "在庫数は必須です")
    @Min(value = 0, message = "在庫数は0以上にしてください")
    private Integer stock;
	
	public Entity_product(){}
	public Entity_product(int id,String name,int price,int stock){
		this.id=id;
		this.name=name;
		this.price=price;
		this.stock = stock;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	
	public Integer getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}
}
