package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ProductController {
	@RequestMapping("/product/top")
	public String  product_top(Model m) {
		return "/product/top";
	}
	
	@RequestMapping("/product/show")
	public String  product_show(Model m) {
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
