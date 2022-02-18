package com.nepaliCravings.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nepaliCravings.demo.model.Subscribers;

@Repository
public interface SubscribersRepository extends JpaRepository <Subscribers, Long > {

	Subscribers findByUserId(long id);
	Subscribers findById(long id);
	Subscribers findBySubscriptionId(long id);
}
