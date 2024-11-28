package com.demoapplication.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CustomerRepository extends JpaRepository<Customer, Integer>  {
	@Query
	boolean existsCustomerByEmail(String email);
	void deleteById(Integer id);
	boolean existsCustomerById(Integer id);
	
}
