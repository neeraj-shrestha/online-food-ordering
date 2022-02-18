package com.nepaliCravings.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nepaliCravings.demo.model.Delivery;
import com.nepaliCravings.demo.repository.DeliveryRepository;

@Service
public class DeliveryServiceImpl implements DeliveryService{
	@Autowired
	private DeliveryRepository deliveryRepository;

	@Override
	public Delivery findById(long id) {
		// TODO Auto-generated method stub
		return deliveryRepository.findById(id);
	}

}
