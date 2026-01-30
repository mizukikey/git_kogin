package com.example.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.DAO_product;
import com.example.demo.model.Entity_product;

@Controller
public class ProductController {
  	private DAO_product dao_product ;
  	
	// DAO_Vegetableのコンストラクター。
  	public ProductController( DAO_product dp ) {
		this.dao_product = dp ;
	}
  	
	@RequestMapping("/product/top")
	public String  product_top(Model m) {
		return "/product/top";
	}
	
	@RequestMapping("/product/show")
	public String  product_show(Model m) {
		List<Entity_product> productlist=dao_product.findAll();
		m.addAttribute("productlist",productlist);
		return "/product/show";
	}
	
	@RequestMapping("/product/input")
	public String  product_input(Model m) {
		return "/product/input";
	}
	@RequestMapping("/product/update")
	public String  product_update(Model m) {
		return "/product/update";
	}
	@RequestMapping("/product/delete")
	public String  product_delete(Model m) {
		return "/product/delete";
	}
	
}
