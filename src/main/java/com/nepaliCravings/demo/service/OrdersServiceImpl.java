package com.nepaliCravings.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nepaliCravings.demo.model.Orders;
import com.nepaliCravings.demo.repository.OrdersRepository;

@Service
public class OrdersServiceImpl implements OrdersService {
	
	@Autowired
	private OrdersRepository ordersRepository;

	@Override
	public Orders findById(long id) {
		return ordersRepository.findById(id);
	}

	@Override
	public List<Orders> findByRestaurantId(long id) {
		
		return ordersRepository.findByRestaurantId(id);
	}

	@Override
	public List<Orders> findByUserId(long id) {
		
		return ordersRepository.findByUserId(id);
	}

	@Override
	public void save(Orders orders) {
		ordersRepository.save(orders);
		
	}

	@Override
	public void delete(Orders order) {
		ordersRepository.delete(order);
		
	}

}
