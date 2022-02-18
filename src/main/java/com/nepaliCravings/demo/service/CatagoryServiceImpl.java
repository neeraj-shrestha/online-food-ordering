package com.nepaliCravings.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nepaliCravings.demo.model.Catagory;
import com.nepaliCravings.demo.repository.CatagoryRepository;

@Service
public class CatagoryServiceImpl implements CatagoryService{
	@Autowired 
	private CatagoryRepository catagoryRepository;

	@Override
	public List<Catagory> findByRestaurantId(long id) {
		// TODO Auto-generated method stub
		return catagoryRepository.findByRestaurantId(id);
	}

}
