package com.example.demo.controller;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
