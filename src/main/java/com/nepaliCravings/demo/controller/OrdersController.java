package com.nepaliCravings.demo.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

import com.nepaliCravings.demo.model.ChargeRequest;
import com.nepaliCravings.demo.model.ChargeRequest.Currency;
import com.nepaliCravings.demo.model.Delivery;
import com.nepaliCravings.demo.model.Orders;
import com.nepaliCravings.demo.model.Restaurant;
import com.nepaliCravings.demo.model.User;
import com.nepaliCravings.demo.service.CartService;
import com.nepaliCravings.demo.service.OrdersService;
import com.nepaliCravings.demo.service.RestaurantService;
import com.nepaliCravings.demo.service.StripeService;
import com.nepaliCravings.demo.service.UserService;
import com.stripe.model.Charge;

@Controller
public class OrdersController {

	@Value("${publickey}")
	private String stripePublicKey;

	@Autowired
	private CartService cartService;
	
	@Autowired
	private OrdersService ordersService;

	@Autowired
	private UserService userService;

	@Autowired
	private RestaurantService restaurantService;

	@Autowired
	private StripeService stripeService;

	@PostMapping("/savingOrder")
	public ModelAndView ordersaving(@ModelAttribute("order") @RequestBody Orders order, HttpServletRequest request) {
		System.out.println(order.toString());
		System.out.println(order.getFoods().getRestaurant().getEmail());
		Principal principal = request.getUserPrincipal();
		User user = userService.findByEmail(principal.getName());
		ModelAndView model = new ModelAndView();
		try {

			Delivery delivery = order.getDelivery();
			delivery.setStatus("pending");
			order.setRestaurant(order.getFoods().getRestaurant());
			order.setDelivery(delivery);
			order.setUser(user);
			System.out.println(order.getDelivery().getStatus());
			System.out.println(order.getDelivery().getDeliveryAddress());
			ordersService.save(order);
			model.addObject("order", new Orders());
			model.addObject("user", user);
			model.addObject("cart", cartService.findByUserId(user.getId()));
			model.addObject("stripePublicKey", stripePublicKey);
			model.setViewName("userCart");
			model.setStatus(HttpStatus.OK);
			return model;
		} catch (Exception e) {
			model.addObject("order", new Orders());
			model.addObject("user", user);
			model.addObject("cart", cartService.findByUserId(user.getId()));
			model.addObject("stripePublicKey", stripePublicKey);
			model.setViewName("userCart");
			model.setStatus(HttpStatus.BAD_GATEWAY);
			return model;
		}

	}

	@PostMapping("/savingOrder/{id}")
	public String savingToCart(Model model, @ModelAttribute("order") @RequestBody Orders order,
			@PathVariable("id") long id, HttpServletRequest request) {
		System.out.println(order.toString());
		System.out.println(model.toString());
		Principal principal = request.getUserPrincipal();
		User user = userService.findByEmail(principal.getName());
		try {
			Delivery delivery = order.getDelivery();
			delivery.setStatus("pending");
			order.setRestaurant(order.getFoods().getRestaurant());
			order.setDelivery(delivery);
			order.setUser(user);
			System.out.println(order.getDelivery().getStatus());
			System.out.println(order.getDelivery().getDeliveryAddress());
			ordersService.save(order);
			model.addAttribute("order", new Orders());
			model.addAttribute("user", userService.findByEmail(principal.getName()));
			model.addAttribute("cart", cartService.findByUserId(user.getId()));
			model.addAttribute("stripePublicKey", stripePublicKey);
			return "redirect:/removecart/" + id;
		} catch (Exception e) {
			model.addAttribute("order", new Orders());
			model.addAttribute("user", userService.findByEmail(principal.getName()));
			model.addAttribute("cart", cartService.findByUserId(user.getId()));
			model.addAttribute("stripePublicKey", stripePublicKey);
			return "userCart";
		}
	}

	@GetMapping("/userorders")
	public String orderOfUsers(Model model, HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		System.out.println(principal.getName());
		User user = userService.findByEmail(principal.getName());
		List<Orders> order = ordersService.findByUserId(user.getId());
		List<Orders> orders = new ArrayList<Orders>();
		for (int i = 0; i < order.size(); i++) {
			if (!order.get(i).getDelivery().getStatus().equalsIgnoreCase("delivered")) {
				orders.add(order.get(i));
			}
		}
		model.addAttribute("user", user);
		model.addAttribute("orders", orders);
		return "userOrders";
	}

	@GetMapping("/restorder")
	public String ordersOfRestaurant(Model model, HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		System.out.println(principal.getName());
		Restaurant res = restaurantService.findByEmail(principal.getName());
		model.addAttribute("restaurant", res);
		model.addAttribute("orders", ordersService.findByRestaurantId(res.getId()));
		return "restOrders";
	}

	@GetMapping("/sendorder/{id}")
	public ModelAndView ordersend(@PathVariable("id") long id, HttpServletRequest request,
			ChargeRequest chargeRequest) {
		System.out.println(id);
		Principal principal = request.getUserPrincipal();
		Restaurant rest = restaurantService.findByEmail(principal.getName());
		ModelAndView model = new ModelAndView();
		Orders order = new Orders();
		try {
			order = ordersService.findById(id);
			model.addObject("restaurant", rest);
			model.addObject("orders", ordersService.findByRestaurantId(rest.getId()));
			System.out.println(order.getId());
				Delivery delivery = order.getDelivery();
				System.out.println(id);
				LocalDateTime deptime = LocalDateTime.now();
				LocalDateTime arivtime = deptime.plusHours(1);
				delivery.setDepartureTime(deptime);
				delivery.setArrivalTime(arivtime);
				delivery.setStatus("sent");
				order.setDelivery(delivery);
				ordersService.save(order);
				model.addObject("orders", ordersService.findByRestaurantId(rest.getId()));
				model.addObject("sendSuccess", "Successfully sent the food");
				model.setViewName("restOrders");
				return model;
			
		} catch (Exception e) {
			model.addObject("restaurant", restaurantService.findByEmail(principal.getName()));
			model.addObject("orders", ordersService.findByRestaurantId(rest.getId()));
			model.addObject("sendError", "Error sending the food");
			model.setViewName("restOrders");
			return model;
		}

	}
	@GetMapping("/sendOrderSuccess")
	public String successOrdersend(Model model, HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		System.out.println(principal.getName());
		Restaurant res = restaurantService.findByEmail(principal.getName());
		model.addAttribute("restaurant", res);
		model.addAttribute("orders", ordersService.findByRestaurantId(res.getId()));
		return "restOrders";
	}

	@GetMapping("/deliveredOrder/{id}")
	public String orderDelivered(@PathVariable("id") long id) {
		Orders order = ordersService.findById(id);
		order.getDelivery().setStatus("delivered");
		ordersService.save(order);
		return "redirect:/userorders";

	}

	@GetMapping("/deleteorder/{id}")
	public String orderDelete(@PathVariable("id") long id) {
		Orders order = ordersService.findById(id);
		ordersService.delete(order);
		return "redirect:/restorder";

	}
	@GetMapping("/cancelorder/{id}")
	public String cancelDelete(@PathVariable("id") long id) {
		Orders order = ordersService.findById(id);
		ordersService.delete(order);
		return "redirect:/restorder";

	}
}
