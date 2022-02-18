package com.nepaliCravings.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nepaliCravings.demo.model.Payment;

@Repository
public interface PaymentRepository extends JpaRepository <Payment, Long > {

}
