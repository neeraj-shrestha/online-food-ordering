package com.nepaliCravings.demo.controller;

import java.security.Principal;
import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.nepaliCravings.demo.model.Cart;
import com.nepaliCravings.demo.model.Orders;
import com.nepaliCravings.demo.model.Restaurant;
import com.nepaliCravings.demo.model.Subscribers;
import com.nepaliCravings.demo.model.Subscription;
import com.nepaliCravings.demo.model.User;
import com.nepaliCravings.demo.service.FoodsService;
import com.nepaliCravings.demo.service.RestaurantService;
import com.nepaliCravings.demo.service.SubCatagoryService;
import com.nepaliCravings.demo.service.SubscribersService;
import com.nepaliCravings.demo.service.SubscriptionService;
import com.nepaliCravings.demo.service.UserService;

@Controller
public class SubscriptionController {
	
	@Value("${publickey}")
    private String stripePublicKey;
	@Autowired
	private SubscriptionService subscriptionService;
	@Autowired
	private SubscribersService subscribersService;
	@Autowired
	private FoodsService foodsService;
	@Autowired
	private SubCatagoryService subCatagoryService;
	
	@Autowired
	private RestaurantService restaurantService;
	@Autowired
	private UserService userService;
	
	@GetMapping("/subscriptionAddition")
	private String subscriptions(Model model,HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		Restaurant rest=restaurantService.findByEmail(principal.getName());
		model.addAttribute("subscription", new Subscription());
		model.addAttribute("restaurant", rest);
		return "subscriptionAddition";
		
	}
	
	@PostMapping("/addingSubscription")
	public String subscriptionAdd(Model model, @ModelAttribute Subscription subscription,HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		Restaurant rest=restaurantService.findByEmail(principal.getName());
		System.out.println(subscription.toString());
		subscription.setRestaurant(rest);
		subscriptionService.save(subscription);
		model.addAttribute("subscription", new Subscription());
		model.addAttribute("restaurant", rest);
		return "subscriptionAddition";
	}

	@PostMapping("/subscribe/{id}")
	public String subscribe(Model model, @PathVariable("id") Long id,HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		User user = userService.findByEmail(principal.getName());
		Subscribers subscribers=new Subscribers();
		Subscribers subscriber=subscribersService.findByUserId(user.getId());
		Subscription subscription=subscriptionService.findById(id);
		if(subscriber!=null) {
			return "redirect:/alreadysuscribed/"+subscription.getRestaurant().getId();
		}else {
		LocalDateTime subscriptiondate=LocalDateTime.now();
		LocalDateTime expriryDate=subscriptiondate.plusMonths(1);
		subscribers.setSubscriptionDate(subscriptiondate);
		subscribers.setSubscriptionExpiryDate(expriryDate);
		subscribers.setSubscription(subscription);
		subscribers.setUser(user);
		subscribersService.save(subscribers);
		return "redirect:/menu/"+subscription.getRestaurant().getId();
		}
	}
	@GetMapping("/alreadysuscribed/{id}")
	public String menus(@PathVariable("id") Long id, Model model, HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		User user = userService.findByEmail(principal.getName());
		Restaurant rest = restaurantService.findById(id);
		model.addAttribute("orders", new Orders());
		model.addAttribute("user", user);
		model.addAttribute("cart", new Cart());
		model.addAttribute("restaurant", rest);
		model.addAttribute("stripePublicKey", stripePublicKey);
		model.addAttribute("subs", subscriptionService.findByRestaurantId(rest.getId()));
		model.addAttribute("subCatagory", subCatagoryService.findByRestaurantId(rest.getId()));
		System.out.println(id);
		model.addAttribute("foods", foodsService.findByRestaurantId(id));
		return "menu";
	}
	
}
