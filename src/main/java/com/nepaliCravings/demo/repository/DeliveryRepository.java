package com.nepaliCravings.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nepaliCravings.demo.model.Delivery;

@Repository
public interface DeliveryRepository extends JpaRepository <Delivery, Long >{
	
	Delivery findById(long id);

}
