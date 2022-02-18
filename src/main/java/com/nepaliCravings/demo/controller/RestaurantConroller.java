package com.nepaliCravings.demo.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.LinkedList;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.nepaliCravings.demo.dto.RestaurantRegistrationDto;
import com.nepaliCravings.demo.model.Foods;
import com.nepaliCravings.demo.model.Messages;
import com.nepaliCravings.demo.model.Restaurant;
import com.nepaliCravings.demo.model.Reviews;
import com.nepaliCravings.demo.model.User;
import com.nepaliCravings.demo.service.FoodsService;
import com.nepaliCravings.demo.service.OrdersService;
import com.nepaliCravings.demo.service.RestaurantService;
import com.nepaliCravings.demo.service.ReviewsService;
import com.nepaliCravings.demo.service.UserService;

@Controller
public class RestaurantConroller {

	@Value("${upoadImg}")
	private String uploadFolder;

	@Autowired
	private RestaurantService restaurantService;

	@Autowired
	private FoodsService foodsService;
	@Autowired
	private UserService userService;

	@Autowired
	private OrdersService ordersService;

	@Autowired
	private ReviewsService reviewsService;

	@GetMapping("/restaurantregistration")
	public String showRegistrationForm(Model model) {
		model.addAttribute("restaurant", new RestaurantRegistrationDto());
		System.out.println("jeek");
		return "restaurantRegistration";
	}

	@PostMapping("/restaurantregistration")
	public @ResponseBody ResponseEntity<?> registerRestaurantAccount(@RequestParam("image") MultipartFile img,
			@ModelAttribute("restaurant") @Valid RestaurantRegistrationDto restDto, BindingResult result, Model model,
			HttpServletRequest request) throws IOException {

		Restaurant existing = restaurantService.findByEmail(restDto.getEmail());

		if (existing != null) {
			result.rejectValue("email", null, "There is already an account registered with that email");
		}
		try {
			// String uploadDirectory = System.getProperty("user.dir") + uploadFolder;
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
				// Save the file locally
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
				stream.write(img.getBytes());
				stream.close();
			} catch (Exception e) {

				e.printStackTrace();
			}
			byte[] imageData = img.getBytes();

			restDto.setImage(imageData);
			System.out.println(restDto.getImage());
			restaurantService.save(restDto);
			HttpHeaders headers = new HttpHeaders();
			headers.add("Location", "/login");
			return new ResponseEntity<>(headers, HttpStatus.FOUND);
		} catch (Exception e) {
			e.printStackTrace();

			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/allrestaurant")
	public List<Restaurant> findallRest() {
		return restaurantService.findAll();
	}

	@GetMapping("/restaurant/{id}")
	@ResponseBody
	void showImage(@PathVariable("id") Long id, HttpServletResponse response, HttpServletRequest request)
			throws ServletException, IOException {
		// Principal principal = request.getUserPrincipal();

		Restaurant item = restaurantService.findById(id);
		response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
		response.getOutputStream().write(item.getImage());
		response.getOutputStream().close();
	}

	@GetMapping("/restaurantindex")
	public String restProfile(Model model, HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		model.addAttribute("restaurant", restaurantService.findByEmail(principal.getName()));
		Restaurant rest = restaurantService.findByEmail(principal.getName());
		// System.out.println(rest.getId());
//		List<Foods> food=foodsService.findByRestaurantId(rest.getId());
//		List<Foods> foods= new ArrayList<Foods>();
		model.addAttribute("messages", new Messages());
		model.addAttribute("foods", foodsService.findByRestaurantId(rest.getId()));
		model.addAttribute("orders", ordersService.findByRestaurantId(rest.getId()));
		model.addAttribute("reviews", reviewsService.findByRestaurantId(rest.getId()));
		return "restaurantindex";
	}

	@GetMapping("/selectrest")
	public String selectRestaurants(Model model, HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		User user = userService.findByEmail(principal.getName());
		model.addAttribute("user", user);
		model.addAttribute("restaurants", restaurantService.findByApprove(true));
		return "slrest";
	}

	@GetMapping("/restaurantReviews")
	public String restReviews(Model model, HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		Restaurant rest = restaurantService.findByEmail(principal.getName());
		List<Reviews> review = reviewsService.findByRestaurantId(rest.getId());
		List<Reviews> tempList = new LinkedList<Reviews>();
		int listSize = review.size();
		System.out.println(listSize);
		for (int i = listSize; i > 0; i--) {
			tempList.add(review.get(i - 1));

		}
		model.addAttribute("reviewsbyuser", tempList);
		model.addAttribute("restaurant", rest);
		return "reviewsByUser";
	}

	@GetMapping("/changePasswordRestaurant")
	public String passwordChange(Model model, HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		Restaurant resta = restaurantService.findByEmail(principal.getName());
		model.addAttribute("restaurant", resta);
		model.addAttribute("rest", new Restaurant());
		return "restaurantPasswordChange";
	}

	@PostMapping("/changePasswordRestaurant")
	public String changePassword(Model model, HttpServletRequest request, Restaurant rest) {
		Principal principal = request.getUserPrincipal();
		Restaurant resta = restaurantService.findByEmail(principal.getName());
		resta.setPassword(rest.getPassword());
		restaurantService.savepassword(resta);
		model.addAttribute("restaurant", resta);
		model.addAttribute("rest", new Restaurant());
		return "restaurantpasswordchange";
	}

	@GetMapping("/changeProfileRestaurant")
	public String changeProfleuser(Model model, HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		Restaurant resta = restaurantService.findByEmail(principal.getName());
		model.addAttribute("restaurant", resta);
		model.addAttribute("rest", new Restaurant());

		return "restaurantProfilechange";
	}

	@PostMapping("/changeProfileRestaurant")
	public ModelAndView updatingUserProfile(HttpServletRequest request, Restaurant rest) {
		Principal principal = request.getUserPrincipal();
		Restaurant resta = restaurantService.findByEmail(principal.getName());
		ModelAndView model = new ModelAndView();
		try {
			resta.setRest_name(rest.getRest_name());
			resta.setUserName(rest.getUserName());
			resta.setPhone(rest.getPhone());
			resta.setAddress(rest.getAddress());
			resta.setEmail(rest.getEmail());
			restaurantService.saveprofile(resta);
			model.addObject("rest", new Restaurant());
			model.addObject("restaurant", resta);
			model.setViewName("restaurantProfile");
			model.setStatus(HttpStatus.OK);
			return model;
		} catch (Exception e) {
			model.addObject("rest", new Restaurant());
			model.addObject("restaurant", resta);
			model.setViewName("restaurantProfile");
			model.setStatus(HttpStatus.BAD_GATEWAY);
			return model;
		}
	}

	@GetMapping("/restaurantimagechange")
	public String changeimage(Model model, HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		Restaurant resta = restaurantService.findByEmail(principal.getName());
		model.addAttribute("restaurant", resta);
		model.addAttribute("rest", new Restaurant());
		return "restaurantProfile";
	}

	@PostMapping("/restaurantimagechange")
	public ModelAndView changeuserimage(@RequestParam("image") MultipartFile img, HttpServletRequest request) {
		System.out.println(img);
		Principal principal = request.getUserPrincipal();
		Restaurant rest = restaurantService.findByEmail(principal.getName());
		ModelAndView model = new ModelAndView();
		if (img.isEmpty()) {
			model.addObject("restaurant", rest);
			model.addObject("rest", new Restaurant());
			model.addObject("empty", "Image cannot be empty");
			model.setViewName("restaurantProfile");
			//model.setStatus(HttpStatus.BAD_REQUEST);
			return model;
		} else {
			// System.out.println(user.toString());
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

				rest.setImage(imageData);
				System.out.println(rest.getImage());
				restaurantService.saveprofile(rest);
				// HttpHeaders headers = new HttpHeaders();
				// headers.add("Location", "/restProfile");
				model.addObject("restaurant", rest);
				model.addObject("rest", new Restaurant());
				model.addObject("success", "update image sucess");
				model.setViewName("restaurantProfile");
				model.setStatus(HttpStatus.OK);
				return model;
			} catch (Exception e) {
				e.printStackTrace();
				model.addObject("rest", new Restaurant());
				model.setViewName("restaurantProfile");
				model.addObject("error", "update image error");
				System.out.println(rest.getImage());
				model.setStatus(HttpStatus.BAD_REQUEST);
				return model;
			}
		}
	}

	@GetMapping("/restProfile")
	public String userprofile(Model model, HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		Restaurant resta = restaurantService.findByEmail(principal.getName());
		model.addAttribute("restaurant", resta);
		model.addAttribute("rest", new Restaurant());
		return "restaurantProfile";
	}

	@GetMapping("/restfood")
	public String foodRest(Model model, HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		Restaurant resta = restaurantService.findByEmail(principal.getName());
		model.addAttribute("restaurant", resta);
		model.addAttribute("foods", foodsService.findByRestaurantId(resta.getId()));
		model.addAttribute("food", new Foods());
		return "restFood";
	}
	
	@GetMapping("/updatefood/{id}")
	public String food(Model model, HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		Restaurant resta = restaurantService.findByEmail(principal.getName());
		model.addAttribute("restaurant", resta);
		model.addAttribute("foods", foodsService.findByRestaurantId(resta.getId()));
		model.addAttribute("food", new Foods());
		return "restFood";
	}

}
