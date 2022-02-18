package com.nepaliCravings.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nepaliCravings.demo.model.Reviews;

@Repository
public interface ReviewsRepository extends JpaRepository <Reviews, Long >{

	Reviews findById(long id);
	List<Reviews> findByRestaurantId(long id);
	List<Reviews> findByUserId(long id);
	
	@Query(value = "SELECT * FROM reviews WHERE restaurant_id=? AND user_id=?",nativeQuery = true)
	Reviews findByrestidanduserid(@Param("restaurant_id") long restaurant_id,@Param("user_id") long user_id);
}
