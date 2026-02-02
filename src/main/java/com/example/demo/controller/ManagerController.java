package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.DAO_manager;
import com.example.demo.model.Entity_manager;
import com.example.demo.model.ManagerService;

@Controller
public class ManagerController {
	
  	private DAO_manager dao_manager;
  	
	// DAO_Vegetableのコンストラクター。
  	public ManagerController( DAO_manager dm ) {
		this.dao_manager = dm ;
	}
	
	@RequestMapping("/manager/top")
	public String  manager_top(Model m) {
		return "/manager/top";
	}
	
  	@Autowired
  	private ManagerService managerService;
  	
	
	@RequestMapping("/manager/show")
	public String  manager_show(Model m) {
		List<Entity_manager> managerlist=dao_manager.findAll();
		m.addAttribute("managerlist",managerlist);
		return "/manager/show";
	}
	
	@GetMapping("/manager/input")
	public String managerInput(Model model) {
	    model.addAttribute("manager", new Entity_manager());
	    return "/manager/input";
	}


	@PostMapping("/manager/input_confirm")
	public String manager_input_confirm(
	        @Validated @ModelAttribute("manager") Entity_manager manager,
	        BindingResult bindingResult,
	        Model model) {

	    // ★ Bean Validation エラー
	    if (bindingResult.hasErrors()) {
	        return "manager/input";
	    }

	    // ★ UNIQUE チェック（ここ）
	    if (managerService.isUserIdDuplicated(manager.getUserId())) {
	        bindingResult.rejectValue(
	            "userId",
	            "duplicate",
	            "※このユーザIDはすでに登録されています"
	        );
	        return "manager/input";
	    }

	    return "manager/input_confirm";
	}

	
	@PostMapping("/manager/input_result")
	public String managerInputResult(
	        Model model,
	        @Valid @ModelAttribute("manager") Entity_manager manager,
	        BindingResult result
	) {
	    if (result.hasErrors()) {
	        return "manager/input";
	    }

	    dao_manager.save(manager);

	    model.addAttribute("m", manager);
	    return "manager/input_result";
	}

	
	@RequestMapping("/manager/update")
	public String  manager_update(Model m) {
		List<Entity_manager> manager_list=dao_manager.findAll();
		m.addAttribute("manager_list",manager_list);
		return "/manager/update";
	}
	

	@RequestMapping("/manager/update_input")
	public String  manager_update_input(Model m, HttpServletRequest req) {
		int id=Integer.parseInt(req.getParameter("id"));
		Optional<Entity_manager> opt=dao_manager.findById(id);
		Entity_manager manager = opt.get();  
		m.addAttribute("manager",manager);
		return "/manager/update_input";
	}

	    // ------------------------
	    // 更新確認画面
	    // ------------------------
	    @PostMapping("/manager/update_confirm")
	    public String manager_update_confirm(
	            @Validated @ModelAttribute("manager") Entity_manager manager,
	            BindingResult bindingResult,
	            Model model) {

	        // Bean Validation エラー
	        if (bindingResult.hasErrors()) {
	            return "manager/update_input";
	        }

	        // UNIQUEチェック（自分自身のIDは除外）
	        if (managerService.isUserIdDuplicatedForUpdate(manager.getUserId(), manager.getId())) {
	            bindingResult.rejectValue("userId", "duplicate", "このユーザIDはすでに登録されています");
	            return "manager/update_input";
	        }

	        model.addAttribute("manager", manager);
	        return "manager/update_confirm";
	    }

	    // ------------------------
	    // 更新結果画面
	    // ------------------------
	    @PostMapping("/manager/update_result")
	    public String manager_update_result(
	            @Valid @ModelAttribute("manager") Entity_manager manager,
	            BindingResult result,
	            Model model) {

	        if (result.hasErrors()) {
	            return "manager/update_input";
	        }

	        dao_manager.save(manager); // ID付きならUPDATE

	        model.addAttribute("m", manager);
	        return "manager/update_result";
	    }
	
	
	@RequestMapping("/manager/delete")
	public String  manager_delete(Model m) {
		List<Entity_manager> manager_list=dao_manager.findAll();
		m.addAttribute("manager_list",manager_list);
		return "/manager/delete";
	}
	
	@RequestMapping("/manager/delete_confirm")
	public String  delete_confirm(HttpServletRequest req,HttpServletRequest r,HttpSession s) {
		int id=Integer.parseInt(req.getParameter("id"));
		Optional<Entity_manager> opt=dao_manager.findById(id);
		Entity_manager result = opt.get();  
		s.setAttribute("result",result);
		return "/manager/delete_confirm";
	}
	
	@RequestMapping("/manager/delete_result")
	public String  delete_result(HttpServletRequest r,HttpSession s) {
		Entity_manager manager = (Entity_manager) s.getAttribute("result");
	    Integer id = manager.getId();
	    dao_manager.deleteById(id);
		return "/manager/delete_result";
	}

}
