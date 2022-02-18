package com.nepaliCravings.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nepaliCravings.demo.model.Restaurant;

@Repository
public interface RestaurantRepository extends JpaRepository <Restaurant, Long >{

	Restaurant findByEmail(String email);
	Restaurant findById(long id);
	List<Restaurant> findAll();
	
	@Query(value = "SELECT * FROM restaurant WHERE approval = ?1 " , nativeQuery = true)
	List<Restaurant> findByApprove(@Param("approval")boolean approve);
	
	@Query(value = "INSERT INTO restaurants_payment(restaurants_id,payment_id) values(?1,?2)",nativeQuery = true)
	//@Query(value = "UPDATE restaurant SET payment = ?1 WHERE id = ?2", nativeQuery = true)
	@Modifying
	@Transactional
	void updateByRestaurant(@Param("restaurants_id") long restaurants_id,@Param("payment_id") long payment_id);
	
}
