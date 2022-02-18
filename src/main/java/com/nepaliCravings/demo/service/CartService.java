package com.nepaliCravings.demo.service;

import java.util.List;

import com.nepaliCravings.demo.model.Cart;

public interface CartService {
	
	
	void save(Cart cart);
	List<Cart> findByRestaurantId(long id);
	List<Cart> findByUserId(long id);
	Cart findById(long id);
	void deleteById(long id);

}
