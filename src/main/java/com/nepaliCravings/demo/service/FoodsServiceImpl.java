package com.nepaliCravings.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nepaliCravings.demo.model.Foods;
import com.nepaliCravings.demo.model.SubCatagory;
import com.nepaliCravings.demo.repository.FoodsRepository;
import com.nepaliCravings.demo.repository.SubCatagoryRepository;

@Service
public class FoodsServiceImpl implements FoodsService {

	@Autowired
	private FoodsRepository foodsRepository;

	@Autowired
	private SubCatagoryRepository subCatagoryRepository;

	@Override
	public List<Foods> findByRestaurantId(long id) {
		// TODO Auto-generated method stub
		return foodsRepository.findByRestaurantId(id);
	}

	@Override
	public void save(Foods food) {
		// TODO Auto-generated method stub
		Foods foods = new Foods();
		SubCatagory subCat = food.getSubCatagory();
		foods.setFoodName(food.getFoodName());
		foods.setDescription(food.getDescription());
		foods.setPrice(food.getPrice());
		foods.setImage(food.getImage());
		foods.setRestaurant(food.getRestaurant());
		foods.setCatagory(food.getCatagory());
		if (food.getSubCatagory().getId() == 0l) {
			subCat.setRestaurant(food.getRestaurant());
			subCat.setCatagory(food.getCatagory());
			subCatagoryRepository.save(subCat);
			foods.setSubCatagory(food.getSubCatagory());
			foodsRepository.save(foods);

		} else {
			foods.setSubCatagory(food.getSubCatagory());
			foodsRepository.save(foods);
		}
	}

	public Foods findById(long id) {
		return foodsRepository.findById(id);
	}

	@Override
	public void delete(Foods foods) {
		// TODO Auto-generated method stub
		foodsRepository.delete(foods);
	}

	@Override
	public void update(Foods food) {
		// TODO Auto-generated method stub
		foodsRepository.save(food);
	}

}
