package com.nepaliCravings.demo.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nepaliCravings.demo.dto.RestaurantRegistrationDto;
import com.nepaliCravings.demo.model.Catagory;
import com.nepaliCravings.demo.model.Restaurant;
import com.nepaliCravings.demo.model.Roles;
import com.nepaliCravings.demo.repository.CatagoryRepository;
import com.nepaliCravings.demo.repository.RestaurantRepository;

@Service
public class RestaurantServiceImpl implements RestaurantService {

	@Autowired
	private RestaurantRepository restaurantRepository;

	@Autowired
	private CatagoryRepository catagoryRepository;

	private PasswordEncoder encrypt = new BCryptPasswordEncoder();

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Restaurant restaurant = restaurantRepository.findByEmail(email);

		if (restaurant == null) {
			throw new BadCredentialsException("Invalid username or password.");
		}
		if (restaurant.getApproval() == false) {
			throw new BadCredentialsException("You are not approved by admin");
		}

		return new org.springframework.security.core.userdetails.User(restaurant.getEmail(), restaurant.getPassword(),
				mapRolesToAuthorities(restaurant.getRoles()));
	}

	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Roles> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}

	@Override
	public Restaurant findByEmail(String email) {
		return restaurantRepository.findByEmail(email);
	}

	@Override
	public List<Restaurant> findAll() {
		return restaurantRepository.findAll();
	}

	@Override
	public Restaurant findById(long id) {

		return restaurantRepository.findById(id);
	}

	@Override
	public void delete(Restaurant rest) {

		restaurantRepository.delete(rest);

	}

	@Override
	public void save(@Valid RestaurantRegistrationDto restDto) {
		Restaurant rest = new Restaurant();
		Catagory catagory = new Catagory();
		Catagory catagory1 = new Catagory();
		Catagory catagory2 = new Catagory();
		rest.setRest_name(restDto.getRest_name());
		rest.setUserName(restDto.getUserName());
		rest.setPhone(restDto.getPhone());
		rest.setAddress(restDto.getAddress());
		rest.setEmail(restDto.getEmail());
		rest.setImage(restDto.getImage());
		rest.setPassword(encrypt.encode(restDto.getPassword()));
		rest.setApproval(false);
		rest.setRoles(Arrays.asList(new Roles("ROLE_RESTAURANT")));
		restaurantRepository.save(rest);
		if (restDto.getVeg() == true && restDto.getNonveg() == true && restDto.getDrinks() == true) {
			System.out.println(restDto.getVeg());
			catagory.setCatagoryName("veg");
			catagory.setRestaurant(rest);
			System.out.println(rest.toString());
			catagoryRepository.save(catagory);
			catagory1.setCatagoryName("nonveg");
			catagory1.setRestaurant(rest);
			catagoryRepository.save(catagory1);
			catagory2.setCatagoryName("Drinks");
			catagory2.setRestaurant(rest);
			catagoryRepository.save(catagory2);
		} else if (restDto.getVeg() == true && restDto.getNonveg() == true) {
			catagory.setCatagoryName("veg");
			catagory.setRestaurant(rest);
			System.out.println(rest.toString());
			catagoryRepository.save(catagory);
			catagory1.setCatagoryName("nonveg");
			catagory1.setRestaurant(rest);
			catagoryRepository.save(catagory1);
		} else if (restDto.getVeg() == true && restDto.getDrinks() == true) {
			catagory.setCatagoryName("veg");
			catagory.setRestaurant(rest);
			System.out.println(rest.toString());
			catagoryRepository.save(catagory);
			catagory2.setCatagoryName("Drinks");
			catagory2.setRestaurant(rest);
			catagoryRepository.save(catagory2);
		} else if (restDto.getNonveg() == true && restDto.getDrinks() == true) {
			catagory1.setCatagoryName("nonveg");
			catagory1.setRestaurant(rest);
			System.out.println(rest.toString());
			catagoryRepository.save(catagory);
			catagory2.setCatagoryName("Drinks");
			catagory2.setRestaurant(rest);
			catagoryRepository.save(catagory2);
		} else {
			catagory.setCatagoryName("veg");
			catagory.setRestaurant(rest);
			System.out.println(rest.toString());
			catagoryRepository.save(catagory);

		}

	}

	@Override
	public void save(Restaurant rest) {
		rest.setApproval(true);
		restaurantRepository.save(rest);

	}

	@Override
	public List<Restaurant> findByApprove(boolean approve) {
		return restaurantRepository.findByApprove(approve);
	}

	@Override
	public void updateRestaurant(Restaurant restaurant) {
		// TODO Auto-generated method stub
	 restaurantRepository.save(restaurant);
//		for (int i = 0; i < restaurant.getPayment().size(); i++) {
//			Payment pay = restaurant.getPayment().get(i);
//			payRestUpdate(restaurant.getId(), pay.getId());
//		}

	}

	public void payRestUpdate(long rid, long pid) {
		restaurantRepository.updateByRestaurant(rid, pid);
	}

	@Override
	public void savepassword(Restaurant rest) {
		rest.setPassword(encrypt.encode(rest.getPassword()));
		restaurantRepository.save(rest);
		
	}

	@Override
	public void saveprofile(Restaurant rest) {
		restaurantRepository.save(rest);
	}

}
