package com.nepaliCravings.demo.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

import com.nepaliCravings.demo.model.Messages;
import com.nepaliCravings.demo.model.Restaurant;
import com.nepaliCravings.demo.model.User;
import com.nepaliCravings.demo.service.MessagesService;
import com.nepaliCravings.demo.service.RestaurantService;
import com.nepaliCravings.demo.service.UserService;

@Controller
public class MessagesController {
	
	@Autowired
	private RestaurantService restaurantService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MessagesService messagesService;
	
	@PostMapping("/messagefromrest")
	public String savemessageofrest(Model model,Messages messagesByrest, HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		try {
			Restaurant rest = restaurantService.findByEmail(principal.getName());
			Messages mesa=messagesService.findByRestaurantId(rest.getId());
			if(mesa!=null) {
				mesa.setStarsweb(messagesByrest.getStarsweb());
				mesa.setMessagefor(messagesByrest.getMessagefor());
				messagesService.save(mesa);
			}else {
			System.out.println(messagesByrest.getMessagefor());
			messagesByrest.setRestaurant(rest);
			messagesService.save(messagesByrest);
			}
			return "redirect:/restaurantindex";
		} catch (Exception e) {
			return "redirect:/usercart";
		}

	}
	
	@PostMapping("/messagefromuser")
	public String savemessageofuser(Model model,Messages messagesByuser, HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		try {
			User user = userService.findByEmail(principal.getName());
			Messages mesa=messagesService.findByUserId(user.getId());
			if(mesa!=null) {
			mesa.setStarsweb(messagesByuser.getStarsweb());
			mesa.setMessagefor(messagesByuser.getMessagefor());
			messagesService.save(mesa);
			}else {
				messagesByuser.setUser(user);
				messagesService.save(messagesByuser);
			}
			return "redirect:/userindex";
		} catch (Exception e) {
			return "redirect:/usercart";
		}

	}

}
