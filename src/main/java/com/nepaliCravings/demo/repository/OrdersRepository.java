package com.nepaliCravings.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nepaliCravings.demo.model.Orders;

@Repository
public interface OrdersRepository extends JpaRepository <Orders, Long >{
	
	Orders findById(long id);
	List<Orders> findByRestaurantId(long id);
	List<Orders> findByUserId(long id);

}
