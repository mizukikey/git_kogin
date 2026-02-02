package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SalesController {
	
	@RequestMapping("/sales/top")
	public String  sales_top(Model m) {
		return "/sales/top";
	}
}
