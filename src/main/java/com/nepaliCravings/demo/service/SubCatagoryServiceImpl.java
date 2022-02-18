package com.nepaliCravings.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nepaliCravings.demo.model.SubCatagory;
import com.nepaliCravings.demo.repository.SubCatagoryRepository;

@Service
public class SubCatagoryServiceImpl implements SubCatagoryService{
	
	@Autowired
	private SubCatagoryRepository subCatagoryRepository;

	@Override
	public void save(SubCatagory subCat) {
		
		subCatagoryRepository.save(subCat);
	}

	@Override
	public List<SubCatagory> findByRestaurantId(long id) {
		// TODO Auto-generated method stub
		return subCatagoryRepository.findByRestaurantId(id);
	}

}
