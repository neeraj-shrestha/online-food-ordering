package com.nepaliCravings.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nepaliCravings.demo.model.Catagory;

@Repository
public interface CatagoryRepository extends JpaRepository <Catagory, Long >{

	List<Catagory> findByRestaurantId(long id);

}
