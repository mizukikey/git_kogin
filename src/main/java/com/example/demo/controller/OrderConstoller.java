package com.example.demo.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.dto.SalesViewDto;
import com.example.demo.model.Entity_customer;
import com.example.demo.repository.SalesRepository;

@Controller
public class OrderConstoller {
	
	@Autowired
	private SalesRepository salesRepository;
	
	@RequestMapping("/order/top")
	public String  order_top(Model m) {
		return "/order/top";
	}
	
	@GetMapping("/order/show")
	public String order_show(HttpSession session, Model model) {
	    Entity_customer customer = (Entity_customer) session.getAttribute("loginCustomer");
	    
	    if (customer == null) {
	        return "redirect:/customer/login"; // 未ログインならログインページへ
	    }

	    // customer_id を使って注文情報を取得
	    Integer customerId = customer.getId();
	    List<SalesViewDto> orders = salesRepository.findByCustomerId(customerId);
	    model.addAttribute("orders", orders);

//	    model.addAttribute("customer", customer);
	    return "order/show";
	}


}
