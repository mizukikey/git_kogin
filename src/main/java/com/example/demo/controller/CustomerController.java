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

import com.example.demo.model.CustomerService;
import com.example.demo.model.DAO_customer;
import com.example.demo.model.Entity_customer;

@Controller
public class CustomerController {
  	private DAO_customer dao_customer ;
  	
	// DAO_Vegetableのコンストラクター。
  	public CustomerController( DAO_customer dc ) {
		this.dao_customer = dc ;
	}
  	@Autowired
  	private CustomerService customerService;
  	
	@RequestMapping("/customer/top")
	public String  customer_top(Model m) {
		return "/customer/top";
	}
	
	@RequestMapping("/customer/show")
	public String  customer_show(Model m) {
		List<Entity_customer> customerlist=dao_customer.findAll();
		m.addAttribute("customerlist",customerlist);
		return "/customer/show";
	}
	
	@GetMapping("/customer/input")
	public String customerInput(Model model) {
	    model.addAttribute("customer", new Entity_customer());
	    return "/customer/input";
	}

	@PostMapping("/customer/input_confirm")
	public String customer_input_confirm(
	        @Validated @ModelAttribute("customer") Entity_customer customer,
	        BindingResult bindingResult,
	        Model model) {

	    // ★ Bean Validation エラー
	    if (bindingResult.hasErrors()) {
	        return "customer/input";
	    }

	    // ★ UNIQUE チェック（ここ）
	    if (customerService.isNameDuplicated(customer.getName())) {
	        bindingResult.rejectValue(
	            "name",
	            "duplicate",
	            "この商品名はすでに登録されています"
	        );
	        return "customer/input";
	    }

	    return "customer/input_confirm";
	}

	
	@PostMapping("/customer/input_result")
	public String customerInputResult(
	        Model model,
	        @Valid @ModelAttribute("customer") Entity_customer customer,
	        BindingResult result
	) {
	    if (result.hasErrors()) {
	        return "customer/input";
	    }

	    dao_customer.save(customer);

	    model.addAttribute("c", customer);
	    return "customer/input_result";
	}

	
	@RequestMapping("/customer/update")
	public String  customer_update(Model m) {
		List<Entity_customer> customer_list=dao_customer.findAll();
		m.addAttribute("customer_list",customer_list);
		return "/customer/update";
	}
	

	@RequestMapping("/customer/update_input")
	public String  customer_update_input(Model m, HttpServletRequest req) {
		int id=Integer.parseInt(req.getParameter("id"));
		Optional<Entity_customer> opt=dao_customer.findById(id);
		Entity_customer customer = opt.get();  
		m.addAttribute("customer",customer);
		return "/customer/update_input";
	}

	    // ------------------------
	    // 更新確認画面
	    // ------------------------
	    @PostMapping("/customer/update_confirm")
	    public String customer_update_confirm(
	            @Validated @ModelAttribute("customer") Entity_customer customer,
	            BindingResult bindingResult,
	            Model model) {

	        // Bean Validation エラー
	        if (bindingResult.hasErrors()) {
	            return "customer/update_input";
	        }

	        // UNIQUEチェック（自分自身のIDは除外）
	        if (customerService.isNameDuplicatedForUpdate(customer.getName(), customer.getId())) {
	            bindingResult.rejectValue("name", "duplicate", "この商品名はすでに登録されています");
	            return "customer/update_input";
	        }

	        model.addAttribute("customer", customer);
	        return "customer/update_confirm";
	    }

	    // ------------------------
	    // 更新結果画面
	    // ------------------------
	    @PostMapping("/customer/update_result")
	    public String customer_update_result(
	            @Valid @ModelAttribute("customer") Entity_customer customer,
	            BindingResult result,
	            Model model) {

	        if (result.hasErrors()) {
	            return "customer/update_input";
	        }

	        dao_customer.save(customer); // ID付きならUPDATE

	        model.addAttribute("p", customer);
	        return "customer/update_result";
	    }
	
	
	@RequestMapping("/customer/delete")
	public String  customer_delete(Model m) {
		List<Entity_customer> customer_list=dao_customer.findAll();
		m.addAttribute("customer_list",customer_list);
		return "/customer/delete";
	}
	
	@RequestMapping("/customer/delete_confirm")
	public String  delete_confirm(HttpServletRequest req,HttpServletRequest r,HttpSession s) {
		int id=Integer.parseInt(req.getParameter("id"));
		Optional<Entity_customer> opt=dao_customer.findById(id);
		Entity_customer result = opt.get();  
		s.setAttribute("result",result);
		return "/customer/delete_confirm";
	}
	
	@RequestMapping("/customer/delete_result")
	public String  delete_result(HttpServletRequest r,HttpSession s) {
		Entity_customer customer = (Entity_customer) s.getAttribute("result");
	    Integer id = customer.getId();
	    dao_customer.deleteById(id);
		return "/customer/delete_result";
	}
	
}
