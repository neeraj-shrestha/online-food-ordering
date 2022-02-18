package com.nepaliCravings.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nepaliCravings.demo.model.Subscription;

@Repository
public interface SubscriptionRepository extends JpaRepository <Subscription, Long > {
	
	List<Subscription> findByRestaurantId(long id);
	
	Subscription findById(long id);
}
