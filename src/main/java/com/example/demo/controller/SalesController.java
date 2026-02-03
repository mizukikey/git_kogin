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
import com.example.demo.model.DAO_product;
import com.example.demo.model.DAO_sales;
import com.example.demo.model.Entity_customer;
import com.example.demo.model.Entity_manager;
import com.example.demo.model.Entity_product;
import com.example.demo.model.ManagerService;
import com.example.demo.model.ProductService;
import com.example.demo.model.SalesService;
import com.example.demo.repository.SalesRepository;


@Controller
public class SalesController {
  	private DAO_sales dao_sales ;
  	private DAO_product dao_product ;
	private SalesRepository salesRepository;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ManagerService managerService;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private SalesService salesService;


//	// DAO_Vegetableのコンストラクター。
//  	public SalesController( DAO_sales ds ) {
//		this.dao_sales = ds ;
//	}
  	
    // ★ ここが重要
    public SalesController(DAO_sales ds, SalesRepository salesRepository) {
        this.dao_sales = ds;
        this.salesRepository = salesRepository;
    }
//  	@Autowired
//  	private CustomerService customerService;
//  	
	@RequestMapping("/sales/top")
	public String  sales_top(Model m) {
		return "/sales/top";
	}
	
	@GetMapping("/sales/show")
	public String salesShow(Model model) {
	    List<SalesViewDto> list = salesRepository.findSalesViewList();
	    model.addAttribute("saleslist", list);
	    return "sales/show";
	}
	
//	@GetMapping("/sales/input")
//	public String salesInput(Model model) {
//	    // フォーム用
//	    model.addAttribute("sales", new Entity_sales());
//	    model.addAttribute("salesDto", new SalesViewDto());
//
//	    // プルダウン用データ
//	    model.addAttribute("productList", productService.findAll());
//	    model.addAttribute("customerList", customerService.findAll());
//	    model.addAttribute("managerList", managerService.findAll());
//	    model.addAttribute("quantityList", List.of());
//	    return "sales/input";
//	}
	
	@GetMapping("/sales/input")
	public String salesInput(
	        @ModelAttribute("salesDto") SalesViewDto dto,
	        Model model) {

	    // プルダウン共通
	    model.addAttribute("productList", productService.findAll());
	    model.addAttribute("customerList", customerService.findAll());
	    model.addAttribute("managerList", managerService.findAll());

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

	    return "sales/input";
	}

	
//	@PostMapping("/sales/input_confirm")
//	public String sales_input_confirm(
//	        BindingResult bindingResult,
//	        Model model) {
//
//	    // ★ Bean Validation エラー
//	    if (bindingResult.hasErrors()) {
//	        return "sales/input";
//	    }
//
//	    return "sales/input_confirm";
//	}
	@PostMapping("/sales/input_confirm")
	public String salesInputConfirm(
	        @Validated @ModelAttribute("salesDto") SalesViewDto dto,
	        BindingResult bindingResult,
	        Model model) {

	    if (bindingResult.hasErrors()) {
	    	setLists(model, dto.getProductId());
	        return "sales/input";
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
	        return "sales/input";
	    }
	    
	    // 表示用データ取得
	    Entity_product product = productService.findById(dto.getProductId());
	    Entity_customer customer = customerService.findById(dto.getCustomerId());
	    Entity_manager manager = managerService.findById(dto.getManagerId());

	    int sumPrice = product.getPrice() * dto.getQuantity();

	    // confirm画面用
	    model.addAttribute("productName", product.getName());
	    model.addAttribute("customerName", customer.getName());
	    model.addAttribute("managerName", manager.getName());
	    model.addAttribute("sumPrice", sumPrice);
	    
	    return "sales/input_confirm";
	}
	
	private void setLists(Model model, Integer productId) {
	    model.addAttribute("productList", productService.findAll());
	    model.addAttribute("customerList", customerService.findAll());
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

	
	@PostMapping("/sales/input_result")
	public String salesInputResult(
	        @ModelAttribute("salesDto") SalesViewDto dto) {
//		salesService.saveFromDto(dto);
		salesService.registerSale(dto);
	    return "sales/input_result";
	}
	
//	@PostMapping("/sales/input_result")
//	public String salesInputResult(
//	        Model model,
//	        BindingResult result,HttpServletRequest req
//	) {
//	    if (result.hasErrors()) {
//	        return "sales/input";
//	    }
//		int id=Integer.parseInt(req.getParameter("id"));
//	    SalesViewDto dto = salesRepository.findSalesViewById(id);
//
////	    dao_sales.save(sales);
////
////	    model.addAttribute("s", sales);
//	    return "sales/input_result";
//	}
	
	@RequestMapping("/sales/update")
	public String  sales_update(Model m) {
	    List<SalesViewDto> list = salesRepository.findSalesViewList();
	    m.addAttribute("saleslist", list);
		return "/sales/update";
	}
	
	
	@RequestMapping("/sales/delete")
	public String  sales_delete(Model m) {
	    List<SalesViewDto> list = salesRepository.findSalesViewList();
	    m.addAttribute("saleslist", list);
		return "/sales/delete";
	}
	
	@RequestMapping("/sales/delete_confirm")
	public String  delete_confirm(HttpServletRequest req,HttpServletRequest r,HttpSession s, Model m) {
		int id=Integer.parseInt(req.getParameter("id"));
	    SalesViewDto dto = salesRepository.findSalesViewById(id);
	    m.addAttribute("sales", dto);
		return "/sales/delete_confirm";
	}
	
	@RequestMapping("/sales/delete_result")
	public String  delete_result(@RequestParam("id") Integer id) {
	    dao_sales.deleteById(id);
		return "/sales/delete_result";
	}
	


}
