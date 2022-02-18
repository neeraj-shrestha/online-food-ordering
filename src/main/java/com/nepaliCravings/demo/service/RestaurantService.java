package com.nepaliCravings.demo.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.nepaliCravings.demo.dto.RestaurantRegistrationDto;
import com.nepaliCravings.demo.model.Restaurant;

public interface RestaurantService extends UserDetailsService {

	Restaurant findByEmail(String email);

	List<Restaurant> findAll();

	Restaurant findById(long id);

	void save(@Valid RestaurantRegistrationDto restDto);

	void save(Restaurant rest);

	void delete(Restaurant rest);

	List<Restaurant> findByApprove(boolean approve);

	void updateRestaurant(Restaurant restaurant);

	void savepassword(Restaurant rest);

	void saveprofile(Restaurant rest);

}
