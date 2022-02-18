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
import org.springframework.web.bind.annotation.RequestBody;

import com.nepaliCravings.demo.model.Cart;
import com.nepaliCravings.demo.model.Foods;
import com.nepaliCravings.demo.model.Orders;
import com.nepaliCravings.demo.model.Restaurant;
import com.nepaliCravings.demo.model.Subscribers;
import com.nepaliCravings.demo.model.User;
import com.nepaliCravings.demo.service.CartService;
import com.nepaliCravings.demo.service.FoodsService;
import com.nepaliCravings.demo.service.SubscribersService;
import com.nepaliCravings.demo.service.UserService;

@Controller
public class CartController {

	@Value("${publickey}")
	private String stripePublicKey;

	@Autowired
	private CartService cartService;

	@Autowired
	private UserService userService;

	@Autowired
	private FoodsService foodsService;

	@Autowired
	private SubscribersService subscribersService;

	@GetMapping("/usercart")
	public String orderOfUser(Model model, HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		User user = userService.findByEmail(principal.getName());
		model.addAttribute("order", new Orders());
		model.addAttribute("user", user);
		model.addAttribute("cart", cartService.findByUserId(user.getId()));
		model.addAttribute("stripePublicKey", stripePublicKey);
		return "userCart";
	}
	@GetMapping("/savetocart/{id}")
	public String userOrder(Model model, HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		User user = userService.findByEmail(principal.getName());
		model.addAttribute("order", new Orders());
		model.addAttribute("user", user);
		model.addAttribute("cart", cartService.findByUserId(user.getId()));
		model.addAttribute("stripePublicKey", stripePublicKey);
		return "userCart";
	}
	@PostMapping("/savetocart/{id}")
	public String savingToCart(Model model, @ModelAttribute("cart") @RequestBody Cart cart, @PathVariable("id") long id,
			HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		User user = userService.findByEmail(principal.getName());
		Foods foods = foodsService.findById(id);
		Restaurant restaurants = foods.getRestaurant();
		Subscribers subscriber = subscribersService.findByUserId(user.getId());
		LocalDateTime currentDate = LocalDateTime.now();
		if(subscriber!=null) {
		if (currentDate.isEqual(subscriber.getSubscriptionExpiryDate())||currentDate.isAfter(subscriber.getSubscriptionExpiryDate())) {
			float totalPrice = cart.getQuantity() * foods.getPrice();
			cart.setTotalPrice(totalPrice);
			cart.setRestaurant(restaurants);
			cart.setFoods(foods);
			cart.setUser(user);
			cartService.save(cart);
			model.addAttribute("order", new Orders());
			model.addAttribute("user", userService.findByEmail(principal.getName()));
			model.addAttribute("cart", cartService.findByUserId(user.getId()));
			return "redirect:/usercart";
		}}
		if (subscribersService.findByUserId(user.getId()) != null) {
			float discountPrice = cart.getQuantity() * foods.getPrice() * subscriber.getSubscription().getRate() / 100;
			float totalPrice = cart.getQuantity() * foods.getPrice() - discountPrice;
			cart.setTotalPrice(totalPrice);
			cart.setRestaurant(restaurants);
			cart.setFoods(foods);
			cart.setUser(user);
			cartService.save(cart);
			model.addAttribute("order", new Orders());
			model.addAttribute("user", userService.findByEmail(principal.getName()));
			model.addAttribute("cart", cartService.findByUserId(user.getId()));
			return "redirect:/usercart";
		} else {
			float totalPrice = cart.getQuantity() * foods.getPrice();
			cart.setTotalPrice(totalPrice);
			cart.setRestaurant(restaurants);
			cart.setFoods(foods);
			cart.setUser(user);
			cartService.save(cart);
			model.addAttribute("order", new Orders());
			model.addAttribute("user", userService.findByEmail(principal.getName()));
			model.addAttribute("cart", cartService.findByUserId(user.getId()));
			return "redirect:/usercart";
		}
	}

	@GetMapping("/removecart/{id}")
	public String removeFromCart(Model model, @PathVariable long id, HttpServletRequest request) {
		cartService.deleteById(id);
		return "redirect:/usercart";
	}

}
