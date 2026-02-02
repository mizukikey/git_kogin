package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {
  	
	@RequestMapping("/customer/login")
	public String  customer_login(Model m) {
		return "/customer/login";
	}
	
	@RequestMapping("/manager/login")
	public String  manager_login(Model m) {
		return "/manager/login";
	}
	
	@RequestMapping("/customer/customer_top")
	public String  customer_top(Model m) {
		return "/customer/customer_top";
	}
	
	@RequestMapping("/manager/manager_top")
	public String  manager_top(Model m) {
		return "/manager/manager_top";
	}
}
