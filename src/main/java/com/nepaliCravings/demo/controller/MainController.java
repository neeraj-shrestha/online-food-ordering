package com.nepaliCravings.demo.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.ArrayList;
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

import com.nepaliCravings.demo.dto.UserRegistrationDto;
import com.nepaliCravings.demo.model.Messages;
import com.nepaliCravings.demo.model.Restaurant;
import com.nepaliCravings.demo.model.Reviews;
import com.nepaliCravings.demo.model.User;
import com.nepaliCravings.demo.service.AdminService;
import com.nepaliCravings.demo.service.RestaurantService;
import com.nepaliCravings.demo.service.ReviewsService;
import com.nepaliCravings.demo.service.UserService;

@Controller
public class MainController {

	@Value("${upoadImg}")
	private String uploadFolder;

	@Autowired
	private RestaurantService restaurantService;

	@Autowired
	private UserService userService;

	@Autowired
	private AdminService adminService;

	@Autowired
	private ReviewsService reviewsService;

	public float averRating(long id) {
		Restaurant rest = restaurantService.findById(id);
		List<Reviews> review = reviewsService.findByRestaurantId(id);
		if (review.isEmpty()) {
			return 0;
		} else {
			float star = 0;
			for (int i = 0; i < review.size(); i++) {
				star = star + review.get(i).getStars();
			}
			System.out.println(star);
			System.out.println(review.size());
			float average = star / review.size();
			System.out.println(average);
			rest.setAverageRating(average);
			restaurantService.save(rest);
			return average;
		}
	}

	@GetMapping("/")
	public String root(HttpServletRequest request, Model model) {

		Principal principal = request.getUserPrincipal();
		if (principal == null) {

			List<Restaurant> rest = restaurantService.findAll();
			List<Restaurant> restaurant = new ArrayList<Restaurant>();
			float aver[] = new float[rest.size()];
			float temp = 0;
			for (int i = 0; i < rest.size(); i++) {
				aver[i] = averRating(rest.get(i).getId());
			}
			for (int i = 0; i < rest.size(); i++) {
				for (int j = i + 1; j < rest.size(); j++) {
					if (aver[i] > aver[j]) {
						temp = aver[i];
						aver[i] = aver[j];
						aver[j] = temp;
					}
				}
			}
			for (int i = rest.size() - 1; i >= 0; i--) {
				if (rest.get(i).getAverageRating() == aver[rest.size() - 1]) {
					restaurant.add(rest.get(i));
					break;
				}
			}
			for (int i = rest.size() - 1; i >= 0; i--) {
				if (rest.get(i).getAverageRating() == aver[rest.size() - 2]) {
					restaurant.add(rest.get(i));
					break;
				}
			}
			for (int i = rest.size() - 1; i >= 0; i--) {
				if (rest.get(i).getAverageRating() == aver[rest.size() - 3]) {
					restaurant.add(rest.get(i));
					break;
				}
			}
			List<Restaurant> res=new ArrayList<Restaurant>();
			
			for (int i = 0; i<3; i++) {
				res.add(rest.get(i));
			
			}
			model.addAttribute("messages", new Messages());
			model.addAttribute("rest", restaurant);
			model.addAttribute("restaurant", res);
			return "index";
		}
		if (userService.findByEmail(principal.getName()) != null) {
			System.out.println(principal.getName());
			return "userindex";
		} else if (restaurantService.findByEmail(principal.getName()) != null) {
			return "restaurantindex";
		} else if (adminService.findByEmail(principal.getName()) != null) {
			return "adminindex";
		} else {
			List<Restaurant> rest = restaurantService.findAll();
			List<Restaurant> restaurant = new ArrayList<Restaurant>();
			float aver[] = new float[rest.size()];
			float temp = 0;
			for (int i = 0; i < rest.size(); i++) {
				aver[i] = averRating(rest.get(i).getId());
			}
			for (int i = 0; i < rest.size(); i++) {
				for (int j = i + 1; j < rest.size(); j++) {
					if (aver[i] > aver[j]) {
						temp = aver[i];
						aver[i] = aver[j];
						aver[j] = temp;
					}
				}
			}
			for (int i = rest.size() - 1; i >= 0; i--) {
				if (rest.get(i).getAverageRating() == aver[rest.size() - 1]) {
					restaurant.add(rest.get(i));
					break;
				}
			}
			for (int i = rest.size() - 1; i >= 0; i--) {
				if (rest.get(i).getAverageRating() == aver[rest.size() - 2]) {
					restaurant.add(rest.get(i));
					break;
				}
			}
			for (int i = rest.size() - 1; i >= 0; i--) {
				if (rest.get(i).getAverageRating() == aver[rest.size() - 3]) {
					restaurant.add(rest.get(i));
					break;
				}
			}
			
			List<Restaurant> res=new ArrayList<Restaurant>();
				
			for (int i = 0; i<3; i++) {
				res.add(rest.get(i));
			
			}
			
			model.addAttribute("messages", new Messages());
			model.addAttribute("rest", restaurant);
			model.addAttribute("restaurant", res);
			return "index";
		}
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/registration")
	public String showRegistrationForm(Model model) {
		model.addAttribute("user", new UserRegistrationDto());
		System.out.println("jeek");
		return "registration";
	}

	@PostMapping("/registration")
	public @ResponseBody ResponseEntity<?> registerUserAccount(@RequestParam("image") MultipartFile img,
			@ModelAttribute("users") @Valid UserRegistrationDto userDto, BindingResult result, Model model,
			HttpServletRequest request) throws IOException {

		User existing = userService.findByEmail(userDto.getEmail());

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

			userDto.setImage(imageData);
			System.out.println(userDto.getImage());
			userService.userSave(userDto);
			HttpHeaders headers = new HttpHeaders();
			headers.add("Location", "/login");
			return new ResponseEntity<>(headers, HttpStatus.FOUND);
		} catch (Exception e) {
			e.printStackTrace();

			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/user/{id}")
	@ResponseBody
	void userImage(@PathVariable("id") long id, HttpServletResponse response, HttpServletRequest request)
			throws ServletException, IOException {

		User item = userService.findById(id);
		response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
		response.getOutputStream().write(item.getImage());
		response.getOutputStream().close();
	}

}
