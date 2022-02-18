package com.nepaliCravings.demo.service;

import java.util.List;

import com.nepaliCravings.demo.model.Payment;

public interface PaymentService {
	
	void save(Payment payment);

	List<Payment> findAll();

}
