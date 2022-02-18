package com.nepaliCravings.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nepaliCravings.demo.model.Cart;
import com.nepaliCravings.demo.repository.CartRepository;

@Service
public class CartServiceImpl implements CartService{
	@Autowired
	private CartRepository cartRepository;

	@Override
	public void save(Cart cart) {
		cartRepository.save(cart);
		
	}

	@Override
	public List<Cart> findByRestaurantId(long id) {
		return cartRepository.findByRestaurantId(id);
	}

	@Override
	public List<Cart> findByUserId(long id) {
		return cartRepository.findByUserId(id);
	}

	@Override
	public Cart findById(long id) {
		return cartRepository.findById(id);
	}

	@Override
	public void deleteById(long id) {
		// TODO Auto-generated method stub
		cartRepository.deleteById(id);
	}
	

}
