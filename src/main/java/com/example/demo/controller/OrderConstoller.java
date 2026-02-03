package com.example.demo.controller;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dto.SalesViewDto;
import com.example.demo.model.Entity_customer;
import com.example.demo.model.SalesService;
import com.example.demo.repository.SalesRepository;

@Controller
public class OrderConstoller {
	
	@Autowired
	private SalesRepository salesRepository;
	
	@Autowired
	private SalesService salesService;
	
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
	
	@RequestMapping("/order/update")
	public String  order_update(HttpSession session,Model m) {
		Entity_customer customer = (Entity_customer) session.getAttribute("loginCustomer");
	    if (customer == null) {
	        return "redirect:/customer/login"; // 未ログインならログインページへ
	    }
	    // customer_id を使って注文情報を取得
	    Integer customerId = customer.getId();
	    List<SalesViewDto> orders = salesRepository.findByCustomerId(customerId);
	    m.addAttribute("orders", orders);
		return "/order/update";
	}
	
	
	@RequestMapping("/order/delete")
	public String  order_delete(HttpSession session,Model m) {
		Entity_customer customer = (Entity_customer) session.getAttribute("loginCustomer");
	    if (customer == null) {
	        return "redirect:/customer/login"; // 未ログインならログインページへ
	    }
	    // customer_id を使って注文情報を取得
	    Integer customerId = customer.getId();
	    List<SalesViewDto> orders = salesRepository.findByCustomerId(customerId);
	    m.addAttribute("orders", orders);
		return "/order/delete";
	}
	
	@RequestMapping("/order/delete_confirm")
	public String  delete_confirm(HttpServletRequest req,HttpServletRequest r,HttpSession s, Model m) {
		int id=Integer.parseInt(req.getParameter("id"));
	    SalesViewDto dto = salesRepository.findSalesViewById(id);
	    m.addAttribute("sales", dto);
		return "/order/delete_confirm";
	}
	
	@RequestMapping("/order/delete_result")
	public String  delete_result(@RequestParam("id") Integer id) {
	    salesService.deleteSale(id);
		return "/order/delete_result";
	}
	


}
