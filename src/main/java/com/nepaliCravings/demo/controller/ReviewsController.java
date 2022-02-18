package com.nepaliCravings.demo.controller;

import java.security.Principal;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.nepaliCravings.demo.model.Restaurant;
import com.nepaliCravings.demo.model.Reviews;
import com.nepaliCravings.demo.model.User;
import com.nepaliCravings.demo.service.RestaurantService;
import com.nepaliCravings.demo.service.ReviewsService;
import com.nepaliCravings.demo.service.UserService;

@Controller
public class ReviewsController {

	@Autowired
	private ReviewsService reviewsService;

	@Autowired
	private UserService userService;

	@Autowired
	private RestaurantService restaurantService;

	//for selecting the restaurant to review for user
	@GetMapping("/selectforReview")
	public String selectRestaurants(Model model, HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		User user = userService.findByEmail(principal.getName());
		model.addAttribute("user", user);
		model.addAttribute("restaurants", restaurantService.findByApprove(true));
		return "selectForReview";
	}

	//for getting reviews page for user
	@GetMapping("/reviews/{id}")
	public String main(Model model, @PathVariable("id") Long id, HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		User user = userService.findByEmail(principal.getName());
		Restaurant rest = restaurantService.findById(id);
		model.addAttribute("user", user);
		model.addAttribute("restaurant", rest);
		model.addAttribute("alreadyReviewed", reviewsService.findByUserId(user.getId()));
		//model.addAttribute("reviewedRest", reviewsService.findByRestaurantId(rest.getId()));
		model.addAttribute("reviews", new Reviews());
		model.addAttribute("rew", reviewsService.findByrestidanduserid(rest.getId(), user.getId()));
		List<Reviews> review = reviewsService.findByUserId(user.getId());
		int listSize = review.size();
		for (int i = 0; i < listSize; i++) {
			Reviews rev = review.get(i);
			Restaurant resta = rev.getRestaurant();
			if (rev != null && resta.getId() != rest.getId()) {
				model.addAttribute("newRev", "not null");
			}
		}
		return "resReviews";
	}

	//saving reviews given by user or posting reviews
	@PostMapping("/reviews/{id}")
	public String saveReviews(Model model, @ModelAttribute Reviews reviews, HttpServletRequest request,
			@PathVariable("id") Long id) {
		Principal principal = request.getUserPrincipal();
		User user = userService.findByEmail(principal.getName());
		System.out.println(reviews.toString());
		Restaurant rest = reviews.getRestaurant();
		if (reviewsService.findByrestidanduserid(rest.getId(), user.getId()) != null) {
			reviews.setUser(user);
			reviewsService.save(reviews);
			model.addAttribute("user", user);
			model.addAttribute("restaurant", rest);
			model.addAttribute("alreadyReviewed", reviewsService.findByUserId(user.getId()));
			model.addAttribute("reviewedRest", reviewsService.findByRestaurantId(rest.getId()));
			model.addAttribute("reviews", new Reviews());
			model.addAttribute("rew", reviewsService.findByrestidanduserid(rest.getId(), user.getId()));
			return "resReviews";
		} else {
			reviews.setUser(user);
			reviewsService.save(reviews);
			model.addAttribute("user", user);
			model.addAttribute("restaurant", rest);
			model.addAttribute("alreadyReviewed", reviewsService.findByUserId(user.getId()));
			model.addAttribute("reviewedRest", reviewsService.findByRestaurantId(rest.getId()));
			model.addAttribute("reviews", new Reviews());
			model.addAttribute("rew", reviewsService.findByrestidanduserid(rest.getId(), user.getId()));
			return "resReviews";
		}
	}

	//for the list of reviews of restaurant for restaurant to view
	@GetMapping("/reviewsofrest")
	public String reviewOfRest(Model model, HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		Restaurant rest = restaurantService.findByEmail(principal.getName());
		model.addAttribute("restaurant", rest);
		List<Reviews> review = reviewsService.findByRestaurantId(rest.getId());
		List<Reviews> tempList = new LinkedList<Reviews>();
		int listSize = review.size();
		for (int i = listSize; i > 0; i--) {
			tempList.add(review.get(i - 1));
		}
		model.addAttribute("reviewsbyuser", tempList);
		return "reviewsByUser";
	}
}
