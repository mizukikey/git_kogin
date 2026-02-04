package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.Entity_customer;
import com.example.demo.model.Entity_manager;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.ManagerRepository;

@Controller
public class LoginController {
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private ManagerRepository managerRepository;
    
    @GetMapping("/customer/login")
    public String showLoginForm() {
        return "customer/login"; // login.html のテンプレートを表示
    }
    
    @PostMapping("/customer/login")
    public String login(
            @RequestParam String userId,
            @RequestParam String password,
            HttpSession session,
            Model model) {

        Entity_customer customer = customerRepository.findByUserIdAndPassword(userId, password);

        if (customer != null) {
            // ログイン成功
            session.setAttribute("loginCustomer", customer);
            return "redirect:/customer/customer_top"; // ホームページへリダイレクト
        } else {
            // ログイン失敗
            model.addAttribute("error", "ユーザーIDまたはパスワードが違います");
            return "customer/login";
        }
    }
    
    @GetMapping("/customer/customer_top")
    public String customerTop(HttpSession session, Model model) {
        // ログインしているか確認
        Entity_customer customer = (Entity_customer) session.getAttribute("loginCustomer");
        if (customer == null) {
            return "redirect:/customer/login"; // 未ログインならログインページへ
        }

        model.addAttribute("customer", customer);
        return "customer/customer_top"; // customer/customer_top.html を表示
    }
    
    @GetMapping("/manager/login")
    public String managershowLoginForm() {
        return "manager/login"; // login.html のテンプレートを表示
    }
    
    @PostMapping("/manager/login")
    public String manager_login(
            @RequestParam String userId,
            @RequestParam String password,
            HttpSession session,
            Model model) {

        Entity_manager manager = managerRepository.findByUserIdAndPassword(userId, password);

        if (manager != null) {
            // ログイン成功
            session.setAttribute("loginManager", manager);
            return "redirect:/manager/manager_top"; // ホームページへリダイレクト
        } else {
            // ログイン失敗
            model.addAttribute("error", "ユーザーIDまたはパスワードが違います");
            return "manager/login";
        }
    }
    
    @GetMapping("/manager/manager_top")
    public String managerTop(HttpSession session, Model model) {
        // ログインしているか確認
        Entity_manager manager = (Entity_manager) session.getAttribute("loginManager");
        if (manager == null) {
            return "redirect:/manager/login"; // 未ログインならログインページへ
        }

        model.addAttribute("manager", manager);
        return "manager/manager_top"; // customer/customer_top.html を表示
    }


}
