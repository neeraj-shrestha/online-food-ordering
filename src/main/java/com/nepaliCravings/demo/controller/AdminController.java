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

import com.nepaliCravings.demo.dto.AdminRegistrationDto;
import com.nepaliCravings.demo.model.Admin;
import com.nepaliCravings.demo.model.Messages;
import com.nepaliCravings.demo.model.Restaurant;
import com.nepaliCravings.demo.model.User;
import com.nepaliCravings.demo.service.AdminService;
import com.nepaliCravings.demo.service.EmailService;
import com.nepaliCravings.demo.service.MessagesService;
import com.nepaliCravings.demo.service.RestaurantService;
import com.nepaliCravings.demo.service.UserService;

@Controller
public class AdminController {

	@Value("${upoadImg}")
	private String uploadFolder;

	@Autowired
	private RestaurantService restaurantService;
	
	@Autowired
	private EmailService emailService;

	@Autowired
	private UserService userService;

	@Autowired
	private AdminService adminService;
	
	@Autowired
	private MessagesService messagesService;

	@GetMapping("/adminindex")
	public String showAdminIndex(Model model, HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		Admin admin = adminService.findByEmail(principal.getName());
		List<Messages> mess=messagesService.findAll();
		List<Messages> messg =new ArrayList<Messages>();
		List<Messages> messag =new ArrayList<Messages>();
		for(int i=0;i<mess.size();i++) {
			if(mess.get(i).getRestaurant()!=null) {
				messg.add(mess.get(i));
			}
			if(mess.get(i).getUser()!=null) {
				messag.add(mess.get(i));
			}
		}
		model.addAttribute("admins", admin);
		model.addAttribute("users", userService.findAll());
		model.addAttribute("rest", restaurantService.findByApprove(true));
		model.addAttribute("restaurant", restaurantService.findByApprove(false));
		model.addAttribute("restmessages", messg);
		model.addAttribute("usermessages", messag);
		return "adminindex";
	}

	@GetMapping("/adminregistration")
	public String showRegistrationForm(Model model) {
		model.addAttribute("admin", new AdminRegistrationDto());
		System.out.println("jeek");
		return "adminRegistration";
	}

	@PostMapping("/adminregistration")
	public @ResponseBody ResponseEntity<?> registerAdminAccount(@RequestParam("image") MultipartFile img,
			@ModelAttribute("users") @Valid AdminRegistrationDto adminDto, BindingResult result, Model model,
			HttpServletRequest request) throws IOException {
		Admin existing = adminService.findByEmail(adminDto.getEmail());
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
			adminDto.setImage(imageData);
			System.out.println(adminDto.getImage());
			adminService.adminSave(adminDto);
			HttpHeaders headers = new HttpHeaders();
			headers.add("Location", "/login");
			return new ResponseEntity<>(headers, HttpStatus.FOUND);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/admin/{id}")
	@ResponseBody
	void showAdminImage(@PathVariable("id") long id, HttpServletResponse response, HttpServletRequest request)
			throws ServletException, IOException {
		Admin item = adminService.findById(id);
		response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
		response.getOutputStream().write(item.getImage());
		response.getOutputStream().close();
	}

	@GetMapping("/unapprovedrestaurant")
	public String unapproveRestaurant(Model model, HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		Admin admin = adminService.findByEmail(principal.getName());
		model.addAttribute("admins", admin);
		model.addAttribute("rest", restaurantService.findByApprove(false));
		return "unapprovedRestaurant";
	}

	@GetMapping("/approvedrestaurant")
	public String approvedRestaurant(Model model, HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		Admin admin = adminService.findByEmail(principal.getName());
		model.addAttribute("admins", admin);
		model.addAttribute("rest", restaurantService.findByApprove(true));
		// emailService.sendSimpleMessage();
		return "approvedRestaurant";
	}

	@GetMapping("/approverestaurant/{id}")
	public String approveRestaurants(@PathVariable("id") long id, Model model, HttpServletRequest request) {
		System.out.println(id);
		try {
			Restaurant rest = restaurantService.findById(id);
			restaurantService.save(rest);
			return "redirect:/restapproveSucess";
		} catch (Exception e) {
			return "redirect:/errorRestapprove";
		}
	}

	@GetMapping("/restapproveSucess")
	public String restaurantApproveSuccess(Model model, HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		Admin admin = adminService.findByEmail(principal.getName());
		model.addAttribute("admins", admin);
		model.addAttribute("rest", restaurantService.findByApprove(false));
		model.addAttribute("restapproveSucess", "restaurant approve is successfull");
		return "unapprovedRestaurant";
	}

	@GetMapping("/errorRestapprove")
	public String errorRestaurantApprove(Model model, HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		Admin admin = adminService.findByEmail(principal.getName());
		model.addAttribute("admins", admin);
		model.addAttribute("rest", restaurantService.findByApprove(false));
		model.addAttribute("errorRestapprove", "cannot approve this restaurant");
		return "unapprovedRestaurant";

	}

	@GetMapping("/deleterestaurant/{id}")
	public String deleteRestaurants(@PathVariable("id") long id, Model model, HttpServletRequest request) {
		System.out.println(id);
		try {
			Restaurant rest = restaurantService.findById(id);
			restaurantService.delete(rest);
			return "redirect:/restDeleteSucess";
		} catch (Exception e) {
			return "redirect:/errorRestDelete";
		}
	}

	@GetMapping("/restDeleteSucess")
	public String restaurantDeleteSuccess(Model model, HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		Admin admin = adminService.findByEmail(principal.getName());
		model.addAttribute("admins", admin);
		model.addAttribute("rest", restaurantService.findByApprove(false));
		model.addAttribute("restapproveSucess", "restaurant delete is successfull");
		return "unapprovedRestaurant";
	}

	@GetMapping("/errorRestDelete")
	public String errorRestaurantDelete(Model model, HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		Admin admin = adminService.findByEmail(principal.getName());
		model.addAttribute("admins", admin);
		model.addAttribute("rest", restaurantService.findByApprove(false));
		model.addAttribute("errorRestapprove", "cannot delete this restaurant");
		return "unapprovedRestaurant";
	}

	@GetMapping("/adminuser")
	public String users(Model model, HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		Admin admin = adminService.findByEmail(principal.getName());
		model.addAttribute("admins", admin);
		model.addAttribute("user", userService.findAll());
		return "adminUser";
	}

	@GetMapping("/userdelete/{id}")
	public String deleteUser(@PathVariable("id") long id, HttpServletRequest request) {
		User user = new User();
		try {
			user = userService.findById(id);
			userService.delete(user);
			return "redirect:/userdelete";
		} catch (Exception e) {
			return "redirect:/userdeleteError";
		}
	}

	@GetMapping("/userdelete")
	public String userDelete(Model model, HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		Admin admin = adminService.findByEmail(principal.getName());
		model.addAttribute("admins", admin);
		model.addAttribute("user", userService.findAll());
		model.addAttribute("deleteduser", "user delete successfull");
		return "adminUser";
	}

	@GetMapping("/userdeleteError")
	public String userErrorDelete(Model model, HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		Admin admin = adminService.findByEmail(principal.getName());
		model.addAttribute("admins", admin);
		model.addAttribute("user", userService.findAll());
		model.addAttribute("deleteduserError", "user delete unsuccessfull");
		return "adminUser";
	}

	@GetMapping("/changePasswordAdmin")
	public String passwordChange(Model model, HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		Admin admin = adminService.findByEmail(principal.getName());
		model.addAttribute("admins", admin);
		model.addAttribute("admin", new Admin());
		return "adminPasswordChange";
	}

	@PostMapping("/changePasswordAdmin")
	public String changePassword(Model model, HttpServletRequest request, @ModelAttribute("admin") Admin admin) {
		Principal principal = request.getUserPrincipal();
		Admin admins = adminService.findByEmail(principal.getName());
		admins.setPassword(admin.getPassword());
		adminService.savepassword(admins);
		model.addAttribute("admins", admins);
		model.addAttribute("admin", new Admin());
		return "adminProfile";
	}

	@GetMapping("/changeProfileAdmin")
	public String changeProfleuser(Model model, HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		Admin admin = adminService.findByEmail(principal.getName());
		model.addAttribute("admins", admin);
		model.addAttribute("admin", new Admin());
		return "adminProfilechange";
	}

	@PostMapping("/changeProfileAdmin")
	public String updatingUserProfile(Model model, HttpServletRequest request, @ModelAttribute("admin") Admin admin) {
		Principal principal = request.getUserPrincipal();
		Admin admins = adminService.findByEmail(principal.getName());
		System.out.println(admin.getFirstName());
		admins.setFirstName(admin.getFirstName());
		admins.setLastName(admin.getLastName());
		admins.setUserName(admin.getUserName());
		admins.setPhone(admin.getPhone());
		admins.setAddress(admin.getAddress());
		admins.setEmail(admin.getEmail());
		adminService.saveprofile(admins);
		return "redirect:/adminProfile";
	}

	
	@GetMapping("/adminimagechange")
	public String changeimage(Model model, HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		Admin admin = adminService.findByEmail(principal.getName());
		model.addAttribute("admins", admin);
		model.addAttribute("admin", new Admin());
		return "adminImageChange";
	}
	

	@PostMapping("/adminimagechange")
	public @ResponseBody ResponseEntity<?> changeuserimage(@RequestParam("image") MultipartFile img, Model model,
			HttpServletRequest request) throws IOException {
		System.out.println(img);
		Principal principal = request.getUserPrincipal();
		Admin admin = adminService.findByEmail(principal.getName());
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
			admin.setImage(imageData);
			System.out.println(admin.getImage());
			adminService.saveprofile(admin);
			HttpHeaders headers = new HttpHeaders();
			headers.add("Location", "/adminProfile");
			return new ResponseEntity<>(headers, HttpStatus.FOUND);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	
	@GetMapping("/adminProfile")
	public String userprofile(Model model, HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		Admin admin = adminService.findByEmail(principal.getName());
		model.addAttribute("admins", admin);
		model.addAttribute("admin", new Admin());
		return "adminProfile";
	}
	@GetMapping("/sendConfirmation/{id}")
	public String sendConfirmation(@PathVariable("id") long id,Model model, HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		Admin admin = adminService.findByEmail(principal.getName());
		Restaurant rest=restaurantService.findById(id);
		String from="cravingsnepali@gmail.com";
		String to=rest.getEmail();
		String subject="confimation for registration";
		String text="please reply to the email for completion of your restaurant";
		emailService.sendSimpleMessage(from, to, subject, text);
		model.addAttribute("admins", admin);
		model.addAttribute("rest", restaurantService.findByApprove(false));
		return "redirect:/unapprovedrestaurant";
	}
	
	@GetMapping("/usermessage")
	public String showUserMessage(Model model, HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		Admin admin = adminService.findByEmail(principal.getName());
		List<Messages> mess=messagesService.findAll();
		List<Messages> messag =new ArrayList<Messages>();
		for(int i=0;i<mess.size();i++) {
			if(mess.get(i).getUser()!=null) {
				messag.add(mess.get(i));
			}
		}
		model.addAttribute("admins", admin);

		model.addAttribute("usermessages", messag);
		model.addAttribute("usemessage", new Messages());
		return "adminviewusermessage";
	}
	@GetMapping("/restmessage")
	public String showRestMessage(Model model, HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		Admin admin = adminService.findByEmail(principal.getName());
		List<Messages> mess=messagesService.findAll();
		List<Messages> messag =new ArrayList<Messages>();
		for(int i=0;i<mess.size();i++) {
			if(mess.get(i).getRestaurant()!=null) {
				messag.add(mess.get(i));
			}
		}
		model.addAttribute("admins", admin);
		model.addAttribute("restmessages", messag);
		model.addAttribute("resmessage", new Messages());
		return "adminviewrestmessage";
	}
	
	@PostMapping("/restReply")
	public String replytoRestaurant(Messages mes,Model model, HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		Admin admin = adminService.findByEmail(principal.getName());
		//Restaurant rest=restaurantService.findById(id);
		System.out.println(mes.getMessagefor());
		System.out.println(mes.getRestaurant());
		String from="cravingsnepali@gmail.com";
		String to=mes.getRestaurant().getEmail();
		String subject="Reply to message";
		String texts=mes.getMessagefor();
		emailService.sendSimpleMessage(from, to, subject, texts);
		model.addAttribute("admins", admin);
		return "redirect:/restmessage";
	}
	@PostMapping("/userReply")
	public String replytoUser(Messages mes,Model model, HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		Admin admin = adminService.findByEmail(principal.getName());
		//Restaurant rest=restaurantService.findById(id);
		System.out.println(mes.getMessagefor());
		System.out.println(mes.getRestaurant());
		String from="cravingsnepali@gmail.com";
		String to=mes.getUser().getEmail();
		String subject="Reply to message";
		String texts=mes.getMessagefor();
		emailService.sendSimpleMessage(from, to, subject, texts);
		model.addAttribute("admins", admin);
		return "redirect:/restmessage";
	}
	
}
