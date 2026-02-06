package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.CustomerService;
import com.example.demo.model.DAO_customer;
import com.example.demo.model.Entity_customer;

@Controller
public class CustomerController {
  	private DAO_customer dao_customer ;
  	
    @Autowired
    private PasswordEncoder passwordEncoder;
  	
	// DAO_Vegetableのコンストラクター。
  	public CustomerController( DAO_customer dc ) {
		this.dao_customer = dc ;
	}
  	@Autowired
  	private CustomerService customerService;
  	
	@RequestMapping("/customer/top")
	public String  customer_top(Model m) {
		return "/customer/top";
	}
	
	@RequestMapping("/customer/show")
	public String  customer_show(Model m) {
		List<Entity_customer> customerlist=dao_customer.findAll();
		m.addAttribute("customerlist",customerlist);
		return "/customer/show";
	}
	
	@GetMapping("/customer/input")
	public String customerInput(Model model) {
	    model.addAttribute("customer", new Entity_customer());
	    return "/customer/input";
	}
	
	
	@PostMapping("/customer/input_confirm")
	public String customer_input_confirm(
	        @ModelAttribute("customer") Entity_customer customer,
	        Model model, HttpSession session, BindingResult bindingResult) {
		
	    if (bindingResult.hasErrors()) {
	        return "customer/input";
	        }

//	    // ★ UNIQUE チェック（ここ）
	    if (customerService.isUserIdDuplicated(customer.getUserId())) {
	        bindingResult.rejectValue(
	            "userId",
	            "duplicate",
	            "※このユーザIDはすでに登録されています"
	        );
	        return "customer/input";
	    }

	    // パスワードをセッションに保持
	    session.setAttribute("rawCustomerPassword", customer.getPassword());

	    return "/customer/input_confirm";
	}

	@PostMapping("/customer/input_result")
	public String customer_Input_Result(
	        @ModelAttribute("customer") Entity_customer customer,
	        Model model,
	        HttpSession session,BindingResult bindingResult) {
		
	    if (bindingResult.hasErrors()) {
        return "customer/input";
        }

	    // セッションからパスワードを取り出してハッシュ化
	    String rawPassword = (String) session.getAttribute("rawCustomerPassword");
	    customer.setPassword(passwordEncoder.encode(rawPassword));

	    dao_customer.saveAndFlush(customer);
	    session.removeAttribute("rawCustomerPassword");

	    model.addAttribute("c", customer);
	    return "customer/input_result";
	}

//	@PostMapping("/customer/input_confirm")
//	public String customer_input_confirm(
//	        @Validated @ModelAttribute("customer") Entity_customer customer,
//	        BindingResult bindingResult,
//	        Model model) {
//
//	    // ★ Bean Validation エラー
//	    if (bindingResult.hasErrors()) {
//	        return "customer/input";
//	    }
//
//	    // ★ UNIQUE チェック（ここ）
//	    if (customerService.isUserIdDuplicated(customer.getUserId())) {
//	        bindingResult.rejectValue(
//	            "userId",
//	            "duplicate",
//	            "※このユーザIDはすでに登録されています"
//	        );
//	        return "customer/input";
//	    }
//
//	    return "customer/input_confirm";
//	}
//
//	
//	@PostMapping("/customer/input_result")
//	public String customerInputResult(
//	        Model model,
//	        @Valid @ModelAttribute("customer") Entity_customer customer,
//	        BindingResult result
//	) {
//	    if (result.hasErrors()) {
//	        return "customer/input";
//	    }
//
//	    dao_customer.save(customer);
//
//	    model.addAttribute("c", customer);
//	    return "customer/input_result";
//	}
	
	@GetMapping("/customer_input")
	public String customer_Input(Model model) {
	    model.addAttribute("customer", new Entity_customer());
	    return "/customer_input";
	}

//	@PostMapping("/customer_input_confirm")
//	public String customerinput_confirm(
//	        @ModelAttribute("customer") Entity_customer customer,
//	        BindingResult bindingResult,
//	        Model model, HttpSession session) {
//
//	    // ★ Bean Validation エラー
//	    if (bindingResult.hasErrors()) {
//	        return "customer_input";
//	    }
//
//	    // ★ UNIQUE チェック（ここ）
//	    if (customerService.isUserIdDuplicated(customer.getUserId())) {
//	        bindingResult.rejectValue(
//	            "userId",
//	            "duplicate",
//	            "※このユーザIDはすでに登録されています"
//	        );
//	        return "customer_input";
//	    }
//	    session.setAttribute("rawCustomerPassword", customer.getPassword());
//
//	    return "/customer_input_confirm";
//	}
//
//	
//	@PostMapping("/customer_input_result")
//	public String customer_InputResult(
//	        Model model,
//	        @ModelAttribute("customer") Entity_customer customer,
//	        BindingResult result, HttpSession session
//	) {
//	    if (result.hasErrors()) {
//	        return "customer_input";
//	    }
//	    // ここでパスワードをハッシュ化
////	    String rawPassword = customer.getPassword();
////	    String hashedPassword = passwordEncoder.encode(rawPassword);
////	    customer.setPassword(hashedPassword);
////
////	    dao_customer.saveAndFlush(customer); // saveAndFlush で確実に DB に反映
//	    
//	    String rawPassword = (String) session.getAttribute("rawCustomerPassword");
//	    customer.setPassword(passwordEncoder.encode(rawPassword));
//	    dao_customer.saveAndFlush(customer);
//	    session.removeAttribute("rawCustomerPassword");
//	    return "customer_input_result";
//	}
	
	@PostMapping("/customer_input_confirm")
	public String customerinput_confirm(
	        @ModelAttribute("customer") Entity_customer customer,
	        Model model, HttpSession session,BindingResult bindingResult) {
		
	    if (bindingResult.hasErrors()) {
	        return "customer/input";
	        }

	    
//	    // ★ UNIQUE チェック（ここ）
	    if (customerService.isUserIdDuplicated(customer.getUserId())) {
	        bindingResult.rejectValue(
	            "userId",
	            "duplicate",
	            "※このユーザIDはすでに登録されています"
	        );
	        return "customer/input";
	    }

	    // パスワードをセッションに保持
	    session.setAttribute("rawCustomerPassword", customer.getPassword());
	    return "/customer_input_confirm";
	}

	@PostMapping("/customer_input_result")
	public String customer_InputResult(
	        @ModelAttribute("customer") Entity_customer customer,
	        Model model,
	        HttpSession session,BindingResult bindingResult) {
		
	    if (bindingResult.hasErrors()) {
	        return "customer/input";
	        }

	    // セッションからパスワードを取り出してハッシュ化
	    String rawPassword = (String) session.getAttribute("rawCustomerPassword");
	    customer.setPassword(passwordEncoder.encode(rawPassword));

	    dao_customer.saveAndFlush(customer);
	    session.removeAttribute("rawCustomerPassword");

	    model.addAttribute("c", customer);
	    return "customer_input_result";
	}



	
	@RequestMapping("/customer/update")
	public String  customer_update(Model m) {
		List<Entity_customer> customer_list=dao_customer.findAll();
		m.addAttribute("customer_list",customer_list);
		return "/customer/update";
	}
	

	@RequestMapping("/customer/update_input")
	public String  customer_update_input(Model m, HttpServletRequest req) {
		int id=Integer.parseInt(req.getParameter("id"));
		Optional<Entity_customer> opt=dao_customer.findById(id);
		Entity_customer customer = opt.get();  
		m.addAttribute("customer",customer);
		return "/customer/update_input";
	}

	    // ------------------------
	    // 更新確認画面
	    // ------------------------
	    @PostMapping("/customer/update_confirm")
	    public String customer_update_confirm(
	            @ModelAttribute("customer") Entity_customer customer,
	            BindingResult bindingResult,
	            Model model) {

	        // Bean Validation エラー
	        if (bindingResult.hasErrors()) {
	            return "customer/update_input";
	        }

	        // UNIQUEチェック（自分自身のIDは除外）
	        if (customerService.isUserIdDuplicatedForUpdate(customer.getUserId(), customer.getId())) {
	            bindingResult.rejectValue("userId", "duplicate", "このユーザIDはすでに登録されています");
	            return "customer/update_input";
	        }

	        model.addAttribute("customer", customer);
	        return "customer/update_confirm";
	    }

	    // ------------------------
	    // 更新結果画面
	    // ------------------------
//	    @PostMapping("/customer/update_result")
//	    public String customer_update_result(
//	            @ModelAttribute("customer") Entity_customer customer,
//	            BindingResult result,
//	            Model model) {
//
//	        if (result.hasErrors()) {
//	            return "customer/update_input";
//	        }
//
//	        dao_customer.save(customer); // ID付きならUPDATE
//
//	        return "customer/update_result";
//	    }
	
	    @PostMapping("/customer/update_result")
	    public String customer_update_result(
	            @ModelAttribute("customer") Entity_customer formCustomer,
	            BindingResult result,
	            Model model) {

	        if (result.hasErrors()) {
	            return "customer/update_input";
	        }

	        // ★ DBの既存データを取得
	        Entity_customer dbCustomer =
	                dao_customer.findById(formCustomer.getId())
	                            .orElseThrow();

	        // ★ 変更を許可する項目だけ上書き
	        dbCustomer.setUserId(formCustomer.getUserId());
	        dbCustomer.setName(formCustomer.getName());
	        dbCustomer.setAddress(formCustomer.getAddress());
	        dbCustomer.setPhone_number(formCustomer.getPhone_number());
	        // ↑ 必要な分だけ
	        dao_customer.save(dbCustomer);
	        return "customer/update_result";
	    }

	
	@RequestMapping("/customer/delete")
	public String  customer_delete(Model m) {
		List<Entity_customer> customer_list=dao_customer.findAll();
		m.addAttribute("customer_list",customer_list);
		return "/customer/delete";
	}
	
	@RequestMapping("/customer/delete_confirm")
	public String  delete_confirm(HttpServletRequest req,HttpServletRequest r,HttpSession s) {
		int id=Integer.parseInt(req.getParameter("id"));
		Optional<Entity_customer> opt=dao_customer.findById(id);
		Entity_customer result = opt.get();  
		s.setAttribute("result",result);
		return "/customer/delete_confirm";
	}
	
	@RequestMapping("/customer/delete_result")
	public String  delete_result(HttpServletRequest r,HttpSession s) {
		Entity_customer customer = (Entity_customer) s.getAttribute("result");
	    Integer id = customer.getId();
	    dao_customer.deleteById(id);
		return "/customer/delete_result";
	}
	
	
	@GetMapping("/customer/mypage/top")
	public String mypage(HttpSession session, Model model) {
	    Entity_customer customer = (Entity_customer) session.getAttribute("loginCustomer");
	    
	    if (customer == null) {
	        return "redirect:/customer/login";
	    }

	    model.addAttribute("customer", customer);
	    return "customer/mypage/top";
	}
	
	@RequestMapping("/customer/mypage/show")
	public String  customer_mypage_show(HttpSession session,Model m) {
	    Entity_customer customer = (Entity_customer) session.getAttribute("loginCustomer");
	    
	    if (customer == null) {
	        return "redirect:/customer/login";
	    }
	    m.addAttribute("customer", customer);
		return "/customer/mypage/show";
	}
	
	@RequestMapping("/customer/mypage/update_input")
	public String  mypage_customer_update_input(Model m, HttpServletRequest req,HttpSession session) {
		Entity_customer customer = (Entity_customer) session.getAttribute("loginCustomer");
//		int id=Integer.parseInt(req.getParameter("id"));
//		Optional<Entity_customer> opt=dao_customer.findById(id);
//		Entity_customer customer = opt.get();  
		m.addAttribute("customer",customer);
		return "/customer/mypage/update_input";
	}

	    // ------------------------
	    // 更新確認画面
	    // ------------------------
	    @PostMapping("/customer/mypage/update_confirm")
	    public String mypage_customer_update_confirm(
	            @ModelAttribute("customer") Entity_customer formCustomer,
	            BindingResult bindingResult,
	            Model model,  HttpSession session) {

	        // Bean Validation エラー
	        if (bindingResult.hasErrors()) {
	            return "customer/mypage/update_input";
	        }

//	        // UNIQUEチェック（自分自身のIDは除外）
//	        if (customerService.isUserIdDuplicatedForUpdate(customer.getUserId(), customer.getId())) {
//	            bindingResult.rejectValue("userId", "duplicate", "このユーザIDはすでに登録されています");
//	            return "customer/mypage/update_input";
//	        }
	        Entity_customer loginCustomer =
	                (Entity_customer) session.getAttribute("loginCustomer");

	        // ★ IDをセッションのものに強制
	        formCustomer.setId(loginCustomer.getId());

	        if (customerService.isUserIdDuplicatedForUpdate(
	                formCustomer.getUserId(), formCustomer.getId())) {
	            bindingResult.rejectValue(
	                "userId", "duplicate", "このユーザIDはすでに登録されています");
	            return "customer/mypage/update_input";
	        }

	        model.addAttribute("customer", formCustomer);
	        return "customer/mypage/update_confirm";
	    }

	    // ------------------------
	    // 更新結果画面
	    // ------------------------
	    @PostMapping("/customer/mypage/update_result")
	    public String mypage_customer_update_result(
	            @ModelAttribute("customer") Entity_customer formCustomer,
	            BindingResult result,
	            Model model,HttpSession session) {

	        if (result.hasErrors()) {
	            return "customer/mypage/update_input";
	        }
	        
	        Entity_customer loginCustomer =
	                (Entity_customer) session.getAttribute("loginCustomer");

	        Entity_customer dbCustomer =
	                dao_customer.findById(loginCustomer.getId())
	                            .orElseThrow();


//	        // ★ DBの既存データを取得
//	        Entity_customer dbCustomer =
//	                dao_customer.findById(formCustomer.getId())
//	                            .orElseThrow();

	        // ★ 変更を許可する項目だけ上書き
	        dbCustomer.setUserId(formCustomer.getUserId());
	        dbCustomer.setName(formCustomer.getName());
	        dbCustomer.setAddress(formCustomer.getAddress());
	        dbCustomer.setPhone_number(formCustomer.getPhone_number());
	        // ↑ 必要な分だけ
	        dao_customer.save(dbCustomer);
	        // ★ セッションも更新（超重要）
	        session.setAttribute("loginCustomer", dbCustomer);

	        return "customer/mypage/update_result";
	    }
	    
	   
		@RequestMapping("/customer/mypage/delete_confirm")
		public String  mypage_delete_confirm(HttpServletRequest req,HttpServletRequest r,HttpSession s) {
			Entity_customer customer = (Entity_customer) s.getAttribute("loginCustomer");
			s.setAttribute("customer",customer);
			return "/customer/mypage/delete_confirm";
		}
		
		@RequestMapping("/customer/mypage/delete_result")
		public String  mypage_delete_result(HttpServletRequest r,HttpSession s) {
			Entity_customer customer = (Entity_customer) s.getAttribute("result");
		    Integer id = customer.getId();
		    dao_customer.deleteById(id);
			return "/customer/mypage/delete_result";
		}

	
}
