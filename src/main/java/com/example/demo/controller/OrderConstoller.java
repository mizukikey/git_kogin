package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class OrderConstoller {
	
	@RequestMapping("/order/top")
	public String  order_top(Model m) {
		return "/order/top";
	}

}
