package com.nepaliCravings.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nepaliCravings.demo.model.Foods;

@Repository
public interface FoodsRepository extends JpaRepository <Foods, Long >{

	List<Foods> findByRestaurantId(long id);
	Foods findById(long id);

}
