package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


@Entity
@Table(name="customer")
public class Entity_customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "名前は必須です")
    @Size(max = 100, message = "名前は100文字以内で入力してください")
    private String name;

    @Size(max = 255, message = "住所は255文字以内で入力してください")
    private String address;
    
    @Size(max = 20, message = "電話番号は20文字以内で入力してください")
    private String phone_number;
    
    @Column(unique = true)
    @NotBlank(message = "ユーザIDは必須です")
    @Size(max = 50, message = "ユーザIDは50文字以内で入力してください")
    private String user_id;
    
    @NotBlank(message = "パスワードは必須です")
    @Size(max = 255, message = "パスワードは255文字以内で入力してください")
    private String password;
    
	
	public Entity_customer(){}
	public Entity_customer(int id,String name,String address,String phone_number,String user_id,String password){
		this.id=id;
		this.name=name;
		this.address=address;
		this.phone_number = phone_number;
		this.user_id=user_id;
		this.password=password;
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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone_number() {
		return phone_number;
	}
	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
