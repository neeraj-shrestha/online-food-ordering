package com.nepaliCravings.demo.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.nepaliCravings.demo.model.Cart;
import com.nepaliCravings.demo.model.Foods;
import com.nepaliCravings.demo.model.Orders;
import com.nepaliCravings.demo.model.Restaurant;
import com.nepaliCravings.demo.model.SubCatagory;
import com.nepaliCravings.demo.model.Subscribers;
import com.nepaliCravings.demo.model.User;
import com.nepaliCravings.demo.service.CatagoryService;
import com.nepaliCravings.demo.service.FoodsService;
import com.nepaliCravings.demo.service.RestaurantService;
import com.nepaliCravings.demo.service.SubCatagoryService;
import com.nepaliCravings.demo.service.SubscribersService;
import com.nepaliCravings.demo.service.SubscriptionService;
import com.nepaliCravings.demo.service.UserService;

@Controller
public class FoodsController {

	@Value("${upoadImg}")
	private String uploadFolder;
	
	@Value("${publickey}")
    private String stripePublicKey;

	@Autowired
	private FoodsService foodsService;
	@Autowired
	private UserService userService;
	@Autowired
	private SubscriptionService subscriptionService;
	@Autowired
	private SubscribersService subscribersService;
	@Autowired
	private RestaurantService restaurantService;

	@Autowired
	private CatagoryService catagoryService;

	@Autowired
	private SubCatagoryService subCatagoryService;

	@GetMapping("/savingfood")
	public ModelAndView savefood(HttpServletRequest request) {
		ModelAndView model = new ModelAndView();
		Principal principal = request.getUserPrincipal();
		Restaurant rest = restaurantService.findByEmail(principal.getName());
		model.addObject("food", new Foods());
		model.addObject("catagory", catagoryService.findByRestaurantId(rest.getId()));
		model.addObject("subcata", subCatagoryService.findByRestaurantId(rest.getId()));
		model.addObject("subCatagories", new SubCatagory());
		model.addObject("principal", principal);
		model.addObject("restaurant", rest);
		model.setViewName("savefood");
		return model;
	}

	@PostMapping("/savefood")
	public @ResponseBody ResponseEntity<?> registerRestaurantAccount(@RequestParam("image") MultipartFile img,
			@ModelAttribute("food") @RequestBody Foods food, BindingResult result, Model model,
			HttpServletRequest request) throws IOException {
		Principal principal = request.getUserPrincipal();
		Restaurant res = restaurantService.findByEmail(principal.getName());
		List<SubCatagory> subCat = subCatagoryService.findByRestaurantId(res.getId());

		try {
			String uploadDirectory = request.getServletContext().getRealPath(uploadFolder);
			String fileName = img.getOriginalFilename();
			String filePath = Paths.get(uploadDirectory, fileName).toString();
			if (fileName == null || fileName.contains("..")) {
				model.addAttribute("invalid", "Sorry! Filename contains invalid path sequence \" + fileName");
				return new ResponseEntity<>("Sorry! Filename contains invalid path sequence " + fileName,
						HttpStatus.BAD_REQUEST);
			}
			try {
				File dir = new File(uploadDirectory);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
				stream.write(img.getBytes());
				stream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			byte[] imageData = img.getBytes();
			if (food.equals(null) || food.getFoodName() == null) {
				HttpHeaders headers = new HttpHeaders();
				headers.add("Location", "/savefood");
				return new ResponseEntity<>(headers, HttpStatus.FOUND);
			} else {
				food.setImage(imageData);
				food.setRestaurant(res);
				SubCatagory subCatagory = food.getSubCatagory();

				for (int i = 0; i < subCat.size(); i++) {
					System.out.println(subCat.toString());
					if (subCatagory.getSubCatagoryName().equalsIgnoreCase(subCat.get(i).getSubCatagoryName())) {
						subCatagory.setId(subCat.get(i).getId());
						break;
					}
				}
				System.out.println(subCatagory.getId());
				food.setSubCatagory(subCatagory);
				System.out.println(food.getSubCatagory().getId());
				foodsService.save(food);
				HttpHeaders headers = new HttpHeaders();
				headers.add("Location", "/aftersave");
				return new ResponseEntity<>(headers, HttpStatus.FOUND);
			}

		} catch (Exception e) {
			e.printStackTrace();

			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/aftersave")
	public String afterFoodSave(Model model, HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		Restaurant rest = restaurantService.findByEmail(principal.getName());
		model.addAttribute("food", new Foods());
		model.addAttribute("restaurant", rest);
		model.addAttribute("afterSave", "Adding food is  successfull!!!");
		return "savefood";
	}

	@GetMapping("/savefood")
	public String sFood(Model model, HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		model.addAttribute("food", new Foods());
		model.addAttribute("principal", principal);
		model.addAttribute("notSave", "sorry! the value is null");
		return "savefood";
	}

	@GetMapping("/menu/{id}")
	public String menus(@PathVariable("id") Long id, Model model, HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		User user = userService.findByEmail(principal.getName());
		Restaurant rest = restaurantService.findById(id);
		Subscribers subscriber=subscribersService.findByUserId(user.getId());
		if(subscriber!=null) {
		LocalDateTime currentTime=LocalDateTime.now();
		if(subscriber.getSubscriptionExpiryDate().isEqual(currentTime)||currentTime.isAfter(subscriber.getSubscriptionExpiryDate())) {
			subscribersService.delete(subscriber);
			subscriber= new Subscribers();
		}
		}
		model.addAttribute("orders", new Orders());
		model.addAttribute("user", user);
		model.addAttribute("cart", new Cart());
		model.addAttribute("restaurant", rest);
		model.addAttribute("stripePublicKey", stripePublicKey);
		model.addAttribute("subs", subscriptionService.findByRestaurantId(rest.getId()));
		model.addAttribute("subscriber", subscriber);
		model.addAttribute("subCatagory", subCatagoryService.findByRestaurantId(rest.getId()));
		System.out.println(id);
		model.addAttribute("foods", foodsService.findByRestaurantId(id));
		return "menu";
	}

	@GetMapping("/food/{id}")
	@ResponseBody
	void showImage(@PathVariable("id") Long id, HttpServletResponse response, HttpServletRequest request)
			throws ServletException, IOException {

		Foods item = foodsService.findById(id);
		response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
		response.getOutputStream().write(item.getImage());
		response.getOutputStream().close();
	}

	@GetMapping("/delete/{id}")
	public ModelAndView delete(@PathVariable("id") long id, HttpServletRequest request) {
		ModelAndView model = new ModelAndView();
		Principal principal = request.getUserPrincipal();
		try {
			Foods foods = foodsService.findById(id);
			Restaurant res = restaurantService.findByEmail(principal.getName());
			foodsService.delete(foods);
			model.addObject("restaurant", res);
			model.addObject("food", new Foods());
			model.addObject("foods", foodsService.findByRestaurantId(res.getId()));
			model.addObject("deleteSucess", "Successfully deleted the food");
			model.setViewName("restFood");
			model.setStatus(HttpStatus.OK);
			return model;

		} catch (Exception e) {
			Restaurant res = restaurantService.findByEmail(principal.getName());
			model.addObject("restaurant", res);
			model.addObject("foods", foodsService.findByRestaurantId(res.getId()));
			model.addObject("food", new Foods());
			model.addObject("deleteError", "error deleting the food");
			model.setViewName("restFood");
			model.setStatus(HttpStatus.BAD_REQUEST);
			return model;
		}

	}

	@PostMapping("/updatefood/{id}")
	public ModelAndView udatefood(@PathVariable("id") long id, @RequestParam("image") MultipartFile img,
			@ModelAttribute @Valid Foods food, BindingResult result, HttpServletRequest request) throws IOException {
		Principal principal = request.getUserPrincipal();
		ModelAndView model = new ModelAndView();
		Restaurant res = restaurantService.findByEmail(principal.getName());
		/*
		 * model.addAttribute("restaurant", res); model.addAttribute("foods",
		 * foodsService.findByRestaurantId(res.getId()));
		 */

		try {
			// String uploadDirectory = System.getProperty("user.dir") + uploadFolder;
			String uploadDirectory = request.getServletContext().getRealPath(uploadFolder);

			String fileName = img.getOriginalFilename();
			String filePath = Paths.get(uploadDirectory, fileName).toString();
			if (fileName == null || fileName.contains("..")) {
				// model.addAttribute("invalid", "Sorry! Filename contains invalid path sequence
				// \" + fileName");
				model.setStatus(HttpStatus.BAD_REQUEST);
				return model;
			}

			try {
				File dir = new File(uploadDirectory);
				if (!dir.exists()) {

					dir.mkdirs();
				}
				// Save the file locally
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
				stream.write(img.getBytes());
				stream.close();
			} catch (Exception e) {

				e.printStackTrace();
			}
			byte[] imageData = img.getBytes();
			try {
				Foods foods = foodsService.findById(id);
				foods.setImage(imageData);
				foods.setFoodName(food.getFoodName());
				foods.setPrice(food.getPrice());
				foods.setDescription(food.getDescription());
				foodsService.update(foods);
				model.addObject("restaurant", res);
				model.addObject("food", new Foods());
				model.addObject("foods", foodsService.findByRestaurantId(res.getId()));
				model.addObject("updateSucess", "Successfully updated the food");
				model.setViewName("restFood");
				model.setStatus(HttpStatus.OK);
				return model;
			} catch (Exception e) {
				model.addObject("restaurant", res);
				model.addObject("food", new Foods());
				model.addObject("foods", foodsService.findByRestaurantId(res.getId()));
				model.addObject("updateError", "Error in updating the food");
				model.setViewName("restFood");
				model.setStatus(HttpStatus.BAD_REQUEST);
				return model;

			}

		} catch (Exception e) {
			e.printStackTrace();
			model.setStatus(HttpStatus.BAD_REQUEST);
			return model;
		}
	}

	@GetMapping("/updateFoodSucess")
	public String menus(Model model, HttpServletRequest request) {
		System.out.println(model);
		Principal principal = request.getUserPrincipal();
		Restaurant res = restaurantService.findByEmail(principal.getName());
		model.addAttribute("restaurant", restaurantService.findByEmail(principal.getName()));
		model.addAttribute("foods", foodsService.findByRestaurantId(res.getId()));
		model.addAttribute("food", new Foods());
		model.addAttribute("updatefood", "Food updated sucessfully");
		return "restFood";
	}

	@GetMapping("/errorupdatefood")
	public String updateError(Model model, HttpServletRequest request) {
		System.out.println(model);
		Principal principal = request.getUserPrincipal();
		Restaurant res = restaurantService.findByEmail(principal.getName());
		model.addAttribute("restaurant", restaurantService.findByEmail(principal.getName()));
		model.addAttribute("foods", foodsService.findByRestaurantId(res.getId()));
		model.addAttribute("food", new Foods());
		model.addAttribute("errorfoodupdate", "food update errror");
		return "restFood";
	}
}
