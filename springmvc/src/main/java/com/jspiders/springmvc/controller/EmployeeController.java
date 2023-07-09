package com.jspiders.springmvc.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.jspiders.springmvc.pojo.AdminPOJO;
import com.jspiders.springmvc.pojo.EmployeePOJO;
import com.jspiders.springmvc.service.AdminService;
import com.jspiders.springmvc.service.EmployeeService;

@Controller
public class EmployeeController {
	
	@Autowired
	private EmployeeService service;
	
	@Autowired
	private AdminService adminService;
	
	//Home Controller
	@GetMapping("/home")
	public String home(@SessionAttribute(name="login",required = false)AdminPOJO login,ModelMap map) {
	
		if (login!=null) {
			return "Home";
		}
		map.addAttribute("msg", "Please Login to Processed...");
		return "Login";
		
	}
	
	//Add Page Controller
	@GetMapping("/add")
	public String addPage(@SessionAttribute(name="login",required = false)AdminPOJO login,ModelMap map) {
		if (login!=null) {
			return "Add";
		}
		map.addAttribute("msg", "Please Login to Processed...");
		return "Login";
	}
	
	//Add Data Controller
	@PostMapping("/add")
	public String add(@SessionAttribute(name="login",required = false)AdminPOJO login,ModelMap map,
			            @RequestParam String name,
						@RequestParam String email,
						@RequestParam long contact,
						@RequestParam String designation,
						@RequestParam double salary
						) {
		if (login!=null) {
			EmployeePOJO employee = service.
					addEmployee(name, email, contact, designation, salary);
			if (employee != null) {
				//Success response
				map.addAttribute("msg", "Data added successfully..!!");
				return "Add";
			}
			//Failure response
			map.addAttribute("msg", "Data not added successfully..!!");
			return "Add";
		}
		map.addAttribute("msg", "Please Login to Processed...");
		return "Login";
		
	}
	
	// Search Page Controller
	@GetMapping("/search")
	public String searchPage(@SessionAttribute(name="login",required = false)AdminPOJO login,ModelMap map) {
		if (login!=null) {
			return "Search";	
		}
		map.addAttribute("msg", "Please Login to Processed...");
		return "Login";
	}

	// Search Data Controller
	@PostMapping("/search")
	public String search(@RequestParam int id, @SessionAttribute(name="login",required = false)AdminPOJO login,ModelMap map) {
		if (login!=null) {
			EmployeePOJO employee = service.searchEmployee(id);
			if (employee != null) {
				// Success response
				map.addAttribute("msg", "Employee data found..!!");
				map.addAttribute("emp", employee);
				return "Search";
			}
			// Failure response
			map.addAttribute("msg", "Data not found..!!");
			return "Search";
		}
		map.addAttribute("msg", "Please Login to Processed...");
		return "Login";
	}
	
	// Remove Page Controller
		@GetMapping("/remove")
		public String removePage(@SessionAttribute(name="login",required = false)AdminPOJO login,ModelMap map) {
		if (login!=null) {
			List<EmployeePOJO> employees = service.searchAllEmployees();
			map.addAttribute("empList", employees);
			return "Remove";
		}
		map.addAttribute("msg", "Please Login to Processed...");
		return "Login";
		}
//
		// Remove Data Controller
		@PostMapping("/remove")
		public String remove(@RequestParam int id, @SessionAttribute(name="login",required = false)AdminPOJO login,ModelMap map) {
			if (login!=null) {
				EmployeePOJO employee = service.searchEmployee(id);
				if (employee != null) {
					// Success response
					service.removeEmployee(id);
					List<EmployeePOJO> employees = service.searchAllEmployees();
					map.addAttribute("empList", employees);
					map.addAttribute("msg", "Data removed successfully..!!");
					return "Remove";
				}
				// Failure response
				List<EmployeePOJO> employees = service.searchAllEmployees();
				map.addAttribute("empList", employees);
				map.addAttribute("msg", "Data does not exist..!!");
				return "Remove";
			}
			map.addAttribute("msg", "Please Login to Processed...");
			return "Login";
		}
		
		
		//Update Page Controller
		@GetMapping("/update")
		public String updatePage(@SessionAttribute(name="login",required = false)AdminPOJO login,ModelMap map) {
			if (login!=null) {
				List<EmployeePOJO> employees = service.searchAllEmployees();
				map.addAttribute("empList", employees);
				
				return "Update";
			}
			map.addAttribute("msg", "Please Login to Processed...");
			return "Login";
		}
		
		//Update Form Controller
		@PostMapping("/update")
		public String updateForm(@RequestParam int id,@SessionAttribute(name="login",required = false)AdminPOJO login,ModelMap map) {
			if (login!=null) {
				EmployeePOJO employee = service.searchEmployee(id);
				if (employee!=null) {
					//Success Response
					map.addAttribute("emp", employee);
					return "Update";
				}
				//Failure
				map.addAttribute("msg", "Data not Found");
				List<EmployeePOJO> employees = service.searchAllEmployees();
				map.addAttribute("empList", employees);
				return "Update";
			}
			map.addAttribute("msg", "Please Login to Processed...");
			return "Login";
		}
		
		
		//Update Data Controller
		@PostMapping("updateData")
		public String update(@SessionAttribute(name="login",required = false)AdminPOJO login,ModelMap map,@RequestParam int id,
				                 @RequestParam String name,
				                 @RequestParam String email,
				                 @RequestParam long contact,
				                 @RequestParam String designation,
				                 @RequestParam double salary) {
		if (login!=null) {
			 EmployeePOJO employee=service.searchEmployee(id); 
				if (employee!=null) {
					//Success Response
					service.updateEmployee(id,name,email,contact,designation,salary);
					map.addAttribute("msg", "Data Updated Successfully");
					List<EmployeePOJO> employees = service.searchAllEmployees();
					map.addAttribute("empList", employees);
					return "Update";
				}
				//Failure
				map.addAttribute("msg", "Data Not Updated ");
					return "Update";
		}
		map.addAttribute("msg", "Please Login to Processed...");
		return "Login";
		}
		
		//Create Admin controller
		@GetMapping("/create")
		public String createAdminPage() {
			
		return "Admin";	
		}
		
		//Create Admin Data Controller
		@PostMapping("/create")
		public String createAdmin(@RequestParam String username,
				                  @RequestParam String password,ModelMap map) {
        AdminPOJO admin=adminService.create(username,password);
        if (admin!=null) {
			//Success Response
        	map.addAttribute("msg", "Account created Successfully");
        	return "Login";
		}
        //Failure Response
        map.addAttribute("msg", "Account Not Created...");
        return "Login";
		}
		
		//Login Controller
		@PostMapping("/login")
		public String login(@RequestParam String username,
				            @RequestParam String password,
				            ModelMap map,HttpServletRequest request) {
			
			AdminPOJO admin=adminService.login(username,password);
			if (admin!=null) {
				//Success Response
				HttpSession session=request.getSession();
				session.setAttribute("login", admin);
				return "Home";
			}
			//Failure Response
			map.addAttribute("msg", "invalid Username and Password");
			return "Login";
		}
		
		//Logout controller
		@GetMapping("/logout")
	   public String logout(ModelMap map,HttpSession session) {
			map.addAttribute("msg", "Logged out Successfully");
			session.invalidate();
			return "Login";
		}
		
		
		
		
}