package com.nepaliCravings.demo.service;

import java.util.List;

import com.nepaliCravings.demo.model.Orders;

public interface OrdersService {
	Orders findById(long id);
	List<Orders> findByRestaurantId(long id);
	List<Orders> findByUserId(long id);
	
	void save(Orders orders);
	void delete(Orders order);

}
