package com.invoicer.customer_service.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.invoicer.customer_service.Entity.Customers;


@Repository
public interface CustomerRepository extends JpaRepository<Customers, Integer> {
  
}