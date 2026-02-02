package com.example.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.dto.SalesViewDto;
import com.example.demo.model.DAO_sales;
import com.example.demo.repository.SalesRepository;


@Controller
public class SalesController {
  	private DAO_sales dao_sales ;
	private SalesRepository salesRepository;

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
	


}
