package com.nepaliCravings.demo.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.nepaliCravings.demo.model.Admin;
import com.nepaliCravings.demo.model.Payment;
import com.nepaliCravings.demo.model.Restaurant;
import com.nepaliCravings.demo.service.AdminService;
import com.nepaliCravings.demo.service.PaymentService;
import com.nepaliCravings.demo.service.RestaurantService;

@Controller
public class PaymentController {
	
	@Autowired
	private PaymentService paymentService;
	@Autowired
	private RestaurantService restaurantService;
	@Autowired
	private AdminService adminService;
	
	@GetMapping("/paymentAddition")
	private String payment(Model model,HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		Admin admin = adminService.findByEmail(principal.getName());
		model.addAttribute("admins", admin);
		model.addAttribute("payment", new Payment());
		return "paymentAddition";
		
	}
	
	@PostMapping("/addingPayment")
	public String paymentAdd(Model model, @ModelAttribute Payment payment) {
		paymentService.save(payment);
		return "paymentAddition";
	}
	
	@GetMapping("/paymentrest")
	public String restaurantPayment(Model model, HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		Restaurant rest=restaurantService.findByEmail(principal.getName());
		System.out.println(rest.getPayment().toString());
		model.addAttribute("restaurant", rest);
		model.addAttribute("payment", paymentService.findAll());
		return "paymentRest";
	}
	
	@PostMapping("/restPayment")
	public String updateRestPayment(Model model,@ModelAttribute Restaurant restaurant,HttpServletRequest request) {
		
		Principal principal = request.getUserPrincipal();
		Restaurant rest=restaurantService.findByEmail(principal.getName());
		rest.getPayment().addAll(restaurant.getPayment());
		//rest.setPayment(restaurant.getPayment());
		System.out.println(rest.getPayment().toString());
		System.out.println(restaurant.getPayment().toString());
		restaurantService.updateRestaurant(rest);
		System.out.println(rest.getPayment().toString());
		model.addAttribute("restaurant", restaurantService.findById(rest.getId()));
		model.addAttribute("payment", paymentService.findAll());
		return "paymentRest";
	}

}
