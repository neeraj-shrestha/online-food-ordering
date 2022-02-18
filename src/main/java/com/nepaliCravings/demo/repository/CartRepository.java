package com.nepaliCravings.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nepaliCravings.demo.model.Cart;

@Repository
public interface CartRepository extends JpaRepository <Cart, Long >{

	List<Cart> findByRestaurantId(long id);

	List<Cart> findByUserId(long id);
	
	Cart findById(long id);

}
