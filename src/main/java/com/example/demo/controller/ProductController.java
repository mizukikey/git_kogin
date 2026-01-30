package com.example.demo.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.DAO_product;
import com.example.demo.model.Entity_product;
import com.example.demo.model.ProductService;

@Controller
public class ProductController {
  	private DAO_product dao_product ;
  	
	// DAO_Vegetableのコンストラクター。
  	public ProductController( DAO_product dp ) {
		this.dao_product = dp ;
	}
  	@Autowired
  	private ProductService productService;
  	
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
	
	@GetMapping("/product/input")
	public String productInput(Model model) {
	    model.addAttribute("product", new Entity_product());
	    return "/product/input";
	}

	@PostMapping("/product/input_confirm")
	public String product_input_confirm(
	        @Validated @ModelAttribute("product") Entity_product product,
	        BindingResult bindingResult,
	        Model model) {

	    // ★ Bean Validation エラー
	    if (bindingResult.hasErrors()) {
	        return "product/input";
	    }

	    // ★ UNIQUE チェック（ここ）
	    if (productService.isNameDuplicated(product.getName())) {
	        bindingResult.rejectValue(
	            "name",
	            "duplicate",
	            "この商品名はすでに登録されています"
	        );
	        return "product/input";
	    }

	    return "product/input_confirm";
	}

	
	@PostMapping("/product/input_result")
	public String productInputResult(
	        Model model,
	        @Valid @ModelAttribute("product") Entity_product product,
	        BindingResult result
	) {
	    if (result.hasErrors()) {
	        return "product/input";
	    }

	    dao_product.save(product);

	    model.addAttribute("p", product);
	    return "product/input_result";
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
