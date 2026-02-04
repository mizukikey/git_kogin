package com.example.demo.controller;

import java.util.List;
import java.util.stream.IntStream;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dto.SalesViewDto;
import com.example.demo.model.CustomerService;
import com.example.demo.model.Entity_customer;
import com.example.demo.model.Entity_manager;
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
	public String  order_top(HttpSession session,Model m) {
		Entity_customer customer =
			    (Entity_customer) session.getAttribute("loginCustomer");

			if (customer == null) {
			    return "redirect:/customer/login";
			}
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
	
//	@GetMapping("/order/input")
//	public String orderInput(
//	        @ModelAttribute("salesDto") SalesViewDto dto,
//	        HttpSession session,
//	        Model model) {
//
//	    // プルダウン共通
//	    model.addAttribute("productList", productService.findAll());
////	    model.addAttribute("customerList", customerService.findAll());
//	    model.addAttribute("managerList", managerService.findAll());
//	    // ログイン中の顧客を取得
//	    Entity_customer customer = (Entity_customer) session.getAttribute("loginCustomer");
//	    
//	    if (customer != null) {
//	        // DTO に顧客IDと顧客名をセット
////	        dto.setCustomerId(customer.getId());
////	        dto.setCustomerName(customer.getName());
//	    	return "redirect:/customer/login";
//	    }
//	    // 数量リスト（商品選択後のみ）
//	    if (dto.getProductId() != null) {
//	        Entity_product product = productService.findById(dto.getProductId());
//	        List<Integer> quantityList = IntStream
//	                .rangeClosed(1, product.getStock())
//	                .boxed()
//	                .toList();
//	        model.addAttribute("quantityList", quantityList);
//	    } else {
//	        model.addAttribute("quantityList", List.of());
//	    }
//
//	    return "order/input";
//	}
	
	@GetMapping("/order/input")
	public String orderInput(
	        @ModelAttribute("salesDto") SalesViewDto dto,
	        HttpSession session,
	        Model model) {

	    Entity_customer customer =
	        (Entity_customer) session.getAttribute("loginCustomer");

	    if (customer == null) {
	        return "redirect:/customer/login";
	    }

	    // プルダウン共通
	    model.addAttribute("productList", productService.findAll());
	    model.addAttribute("managerList", managerService.findAll());

	    // 顧客はログイン情報から固定
	    dto.setCustomerId(customer.getId());
	    dto.setCustomerName(customer.getName());

	    // 数量リスト
	    if (dto.getProductId() != null) {
	        Entity_product product =
	            productService.findById(dto.getProductId());

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

	
	@PostMapping("/order/input_confirm")
	public String orderInputConfirm(
	        @Validated @ModelAttribute("salesDto") SalesViewDto dto,
	        BindingResult bindingResult,
	        Model model,  HttpSession session) {
		
	    // 表示用データ取得
	    Entity_product product = productService.findById(dto.getProductId());
	    Entity_customer customer = (Entity_customer) session.getAttribute("loginCustomer");
	    Entity_manager manager = managerService.findById(dto.getManagerId());

	    int sumPrice = product.getPrice() * dto.getQuantity();
	    
	    if (customer == null) {
	        return "redirect:/customer/login";
	    }
	    
	    dto.setCustomerId(customer.getId());
	    dto.setCustomerName(customer.getName());

	    if (bindingResult.hasErrors()) {
	    	setLists(model, dto.getProductId());
	        return "order/input";
	    }
	    try {
	        salesService.validateStock(dto.getProductId(), dto.getQuantity());
	    } catch (IllegalArgumentException e) {
	        bindingResult.rejectValue(
	            "quantity",
	            "stock.over",
	            e.getMessage()
	        );
	        setLists(model, dto.getProductId());
	        return "order/input";
	    }
	    
	    // confirm画面用
	    model.addAttribute("productName", product.getName());
	    model.addAttribute("managerName", manager.getName());
	    model.addAttribute("sumPrice", sumPrice);
	    return "order/input_confirm";
	}
	
	private void setLists(Model model, Integer productId) {
	    model.addAttribute("productList", productService.findAll());
	    model.addAttribute("managerList", managerService.findAll());
	    
	    if (productId != null) {
	        Entity_product product = productService.findById(productId);

	        List<Integer> quantityList = IntStream
	            .rangeClosed(1, product.getStock())
	            .boxed()
	            .toList();

	        model.addAttribute("quantityList", quantityList);
	    } else {
	        model.addAttribute("quantityList", List.of());
	    }
	}

	
	@PostMapping("/order/input_result")
	public String orderInputResult(
	        @ModelAttribute("salesDto") SalesViewDto dto,  HttpSession session) {
		
	    Entity_customer customer =
	            (Entity_customer) session.getAttribute("loginCustomer");

	    if (customer == null) {
	        return "redirect:/customer/login";
	    }

	    dto.setCustomerId(customer.getId());
	    salesService.registerSale(dto);
	    return "order/input_result";
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
	
//	@GetMapping("/order/update_input")
//	public String updateInput(
//	        @RequestParam Integer id,
//	        @RequestParam(required = false) Integer productId,
//	        HttpSession session,
//	        Model model) {
//
//	    Entity_customer customer =
//	        (Entity_customer) session.getAttribute("loginCustomer");
//
//	    if (customer == null) {
//	        return "redirect:/customer/login";
//	    }
//
//	    SalesViewDto dto =
//	        salesRepository.findByIdForCustomer(id, customer.getId());
//
//	    if (dto == null) {
//	        return "redirect:/order/update";
//	    }
//
//	    // ★ 商品を変更した場合は上書き
//	    if (productId != null) {
//	        dto.setProductId(productId);
//	    }
//
//	    model.addAttribute("salesDto", dto);
//	    model.addAttribute("productList", productService.findAll());
//	    model.addAttribute("managerList", managerService.findAll());
//
//	    Entity_product product =
//	        productService.findById(dto.getProductId());
//
//	    model.addAttribute(
//	        "quantityList",
//	        IntStream.rangeClosed(1, product.getStock())
//	                 .boxed()
//	                 .toList()
//	    );
//
//	    return "order/update_input";
//	}

	
	@PostMapping("/order/update_confirm")
	public String updateConfirm(
	        @Validated @ModelAttribute("salesDto") SalesViewDto dto,
	        BindingResult result,
	        HttpSession session,
	        Model model) {

	    Entity_customer customer =
	        (Entity_customer) session.getAttribute("loginCustomer");

	    if (customer == null) {
	        return "redirect:/customer/login";
	    }

	    // customer は session で固定
	    dto.setCustomerId(customer.getId());
	    dto.setCustomerName(customer.getName());

	    if (result.hasErrors()) {
	        setLists(model, dto.getProductId());
	        return "order/update_input";
	    }

	    Entity_product product =
	        productService.findById(dto.getProductId());

	    int sumPrice = product.getPrice() * dto.getQuantity();
	    

    	Entity_manager manager =
    	    managerService.findById(dto.getManagerId());

    	// ★ DTO に詰める
    	dto.setProductName(product.getName());
    	dto.setManagerName(manager.getName());
    	dto.setSumPrice(sumPrice);

    	// confirm用
    	model.addAttribute("salesDto", dto);

	    return "order/update_confirm";
	}
	
	@PostMapping("/order/update_result")
	public String updateResult(
	        @ModelAttribute("salesDto") SalesViewDto dto,
	        HttpSession session) {

	    Entity_customer customer =
	        (Entity_customer) session.getAttribute("loginCustomer");

	    if (customer == null) {
	        return "redirect:/customer/login";
	    }

	    salesService.updateSale(dto);

	    return "order/update_result";
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
