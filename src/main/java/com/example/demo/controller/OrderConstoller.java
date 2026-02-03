package com.example.demo.controller;

import java.util.List;
import java.util.stream.IntStream;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dto.SalesViewDto;
import com.example.demo.model.CustomerService;
import com.example.demo.model.Entity_customer;
import com.example.demo.model.Entity_product;
import com.example.demo.model.ManagerService;
import com.example.demo.model.ProductService;
import com.example.demo.model.SalesService;
import com.example.demo.repository.SalesRepository;

@Controller
public class OrderConstoller {
	
	@Autowired
	private SalesRepository salesRepository;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ManagerService managerService;
	
	@Autowired
	private CustomerService customerService;
	
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
	
	@GetMapping("/order/input")
	public String orderInput(
	        @ModelAttribute("salesDto") SalesViewDto dto,
	        HttpSession session,
	        Model model) {

	    // プルダウン共通
	    model.addAttribute("productList", productService.findAll());
//	    model.addAttribute("customerList", customerService.findAll());
	    model.addAttribute("managerList", managerService.findAll());
	    // ログイン中の顧客を取得
	    Entity_customer customer = (Entity_customer) session.getAttribute("loginCustomer");
	    
	    if (customer != null) {
	        // DTO に顧客IDと顧客名をセット
	        dto.setCustomerId(customer.getId());
	        dto.setCustomerName(customer.getName());
	    }
	    // 数量リスト（商品選択後のみ）
	    if (dto.getProductId() != null) {
	        Entity_product product = productService.findById(dto.getProductId());
	        List<Integer> quantityList = IntStream
	                .rangeClosed(1, product.getStock())
	                .boxed()
	                .toList();
	        model.addAttribute("quantityList", quantityList);
	    } else {
	        model.addAttribute("quantityList", List.of());
	    }

	    return "order/input";
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
