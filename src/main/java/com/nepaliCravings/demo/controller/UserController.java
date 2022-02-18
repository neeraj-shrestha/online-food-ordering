package com.nepaliCravings.demo.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.nepaliCravings.demo.model.ChargeRequest;
import com.nepaliCravings.demo.model.ChargeRequest.Currency;
import com.nepaliCravings.demo.model.Messages;
import com.nepaliCravings.demo.model.Restaurant;
import com.nepaliCravings.demo.model.Reviews;
import com.nepaliCravings.demo.model.User;
import com.nepaliCravings.demo.service.MessagesService;
import com.nepaliCravings.demo.service.RestaurantService;
import com.nepaliCravings.demo.service.ReviewsService;
import com.nepaliCravings.demo.service.StripeService;
import com.nepaliCravings.demo.service.UserService;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;

@Controller
public class UserController {

	@Value("${publickey}")
	private String stripePublicKey;
	@Value("${upoadImg}")
	private String uploadFolder;

	@Autowired
	private StripeService paymentsService;

	@Autowired
	private RestaurantService restaurantService;

	@Autowired
	private UserService userService;

	@Autowired
	private ReviewsService reviewsService;
	@Autowired
	private MessagesService messagesService;

	@PostMapping("/charge")
	public String charge(ChargeRequest chargeRequest, Model model) throws StripeException {
		chargeRequest.setDescription("Example charge");
		chargeRequest.setCurrency(Currency.EUR);
		Charge charge = paymentsService.charge(chargeRequest);
		model.addAttribute("id", charge.getId());
		model.addAttribute("status", charge.getStatus());
		model.addAttribute("chargeId", charge.getId());
		model.addAttribute("balance_transaction", charge.getBalanceTransaction());
		return "result";
	}

	@RequestMapping("/checkout")
	public String checkout(Model model) {
		model.addAttribute("amount", 50 * 100); // in cents
		model.addAttribute("stripePublicKey", stripePublicKey);
		model.addAttribute("currency", ChargeRequest.Currency.EUR);
		return "checkout";
	}

	@GetMapping("/userindex")
	public String userHomePage(Model model, HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		User user = userService.findByEmail(principal.getName());
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
		model.addAttribute("messages", new Messages());
		model.addAttribute("rest", restaurant);
		model.addAttribute("restaurant", rest);
		model.addAttribute("user", user);
		return "userindex";

	}

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

	@GetMapping("/changePasswordUser")
	public String passwordChange(Model model, HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		User user = userService.findByEmail(principal.getName());
		model.addAttribute("users", user);
		model.addAttribute("user", new User());
		return "userpasswordchange";
	}

	@PostMapping("/changePasswordUser")
	public String changePassword(Model model, HttpServletRequest request, @ModelAttribute("user") User user) {
		Principal principal = request.getUserPrincipal();
		User users = userService.findByEmail(principal.getName());
		users.setPassword(user.getPassword());
		userService.savepassword(users);
		model.addAttribute("user", users);
		return "userpasswordchange";
	}

	@GetMapping("/changeProfileUser")
	public String changeProfleuser(Model model, HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		User user = userService.findByEmail(principal.getName());
		model.addAttribute("users", user);
		model.addAttribute("user", new User());

		return "userProfilechange";
	}

	@PostMapping("/changeProfileUser")
	public String updatingUserProfile(Model model, HttpServletRequest request, @ModelAttribute("user") User user) {
		Principal principal = request.getUserPrincipal();
		User users = userService.findByEmail(principal.getName());
		System.out.println(user.getFirstName());
		users.setFirstName(user.getFirstName());
		users.setLastName(user.getLastName());
		users.setUserName(user.getUserName());
		users.setPhone(user.getPhone());
		users.setAddress(user.getAddress());
		users.setEmail(user.getEmail());
		userService.saveprofile(users);
		return "redirect:/userProfile";
	}

	@GetMapping("/userimagechange")
	public String changeimage(Model model, HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		User users = userService.findByEmail(principal.getName());
		model.addAttribute("users", users);
		model.addAttribute("user", new User());
		return "userImageChange";
	}

	@PostMapping("/userimagechange")
	public @ResponseBody ResponseEntity<?> changeuserimage(@RequestParam("image") MultipartFile img, Model model,
			HttpServletRequest request) throws IOException {
		System.out.println(img);
		Principal principal = request.getUserPrincipal();
		User users = userService.findByEmail(principal.getName());
		// System.out.println(user.toString());
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

			users.setImage(imageData);
			System.out.println(users.getImage());
			userService.saveprofile(users);
			HttpHeaders headers = new HttpHeaders();
			headers.add("Location", "/userProfile");
			return new ResponseEntity<>(headers, HttpStatus.FOUND);
		} catch (Exception e) {
			e.printStackTrace();

			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/userProfile")
	public String userprofile(Model model, HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		User user = userService.findByEmail(principal.getName());
		model.addAttribute("users", user);
		model.addAttribute("user", new User());

		return "userProfile";
	}

}
