package com.nepaliCravings.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nepaliCravings.demo.model.SubCatagory;

@Repository
public interface SubCatagoryRepository extends JpaRepository <SubCatagory, Long >{

	List<SubCatagory> findByRestaurantId(long id);

}
