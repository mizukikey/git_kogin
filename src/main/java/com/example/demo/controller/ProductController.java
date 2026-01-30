package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
		List<Entity_product> product_list=dao_product.findAll();
		m.addAttribute("product_list",product_list);
		return "/product/update";
	}
	

	@RequestMapping("/product/update_input")
	public String  product_update_input(Model m, HttpServletRequest req) {
		int id=Integer.parseInt(req.getParameter("id"));
		Optional<Entity_product> opt=dao_product.findById(id);
		Entity_product product = opt.get();  
		m.addAttribute("product",product);
		return "/product/update_input";
	}

	    // ------------------------
	    // 更新確認画面
	    // ------------------------
	    @PostMapping("/product/update_confirm")
	    public String product_update_confirm(
	            @Validated @ModelAttribute("product") Entity_product product,
	            BindingResult bindingResult,
	            Model model) {

	        // Bean Validation エラー
	        if (bindingResult.hasErrors()) {
	            return "product/update_input";
	        }

	        // UNIQUEチェック（自分自身のIDは除外）
	        if (productService.isNameDuplicatedForUpdate(product.getName(), product.getId())) {
	            bindingResult.rejectValue("name", "duplicate", "この商品名はすでに登録されています");
	            return "product/update_input";
	        }

	        model.addAttribute("product", product);
	        return "product/update_confirm";
	    }

	    // ------------------------
	    // 更新結果画面
	    // ------------------------
	    @PostMapping("/product/update_result")
	    public String product_update_result(
	            @Valid @ModelAttribute("product") Entity_product product,
	            BindingResult result,
	            Model model) {

	        if (result.hasErrors()) {
	            return "product/update_input";
	        }

	        dao_product.save(product); // ID付きならUPDATE

	        model.addAttribute("p", product);
	        return "product/update_result";
	    }
	
	
	@RequestMapping("/product/delete")
	public String  product_delete(Model m) {
		List<Entity_product> product_list=dao_product.findAll();
		m.addAttribute("product_list",product_list);
		return "/product/delete";
	}
	
	@RequestMapping("/product/delete_confirm")
	public String  delete_confirm(HttpServletRequest req,HttpServletRequest r,HttpSession s) {
		int id=Integer.parseInt(req.getParameter("id"));
		Optional<Entity_product> opt=dao_product.findById(id);
		Entity_product result = opt.get();  
		s.setAttribute("result",result);
		return "/product/delete_confirm";
	}
	
	@RequestMapping("/product/delete_result")
	public String  delete_result(HttpServletRequest r,HttpSession s) {
		Entity_product product = (Entity_product) s.getAttribute("result");
	    Integer id = product.getId();
	    dao_product.deleteById(id);
		return "/product/delete_result";
	}
	
}
