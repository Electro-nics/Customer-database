package com.demoapplication.customer;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
@Qualifier("jpa")
@Repository
public class CustomerJPADataAccessServices implements CustomerDao{
	
	private final CustomerRepository customerRepository;
	public CustomerJPADataAccessServices(CustomerRepository customerRepository) {
		this.customerRepository= customerRepository;
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Customer> selectAllCustomers() {
		// TODO Auto-generated method stub
		return customerRepository.findAll();
	}

	@Override
	public Optional<Customer> selectCustomersById(Integer id) {
		// TODO Auto-generated method stub
		return customerRepository.findById(id);
	}

	@Override
	public void insertCustomer(Customer customer) {
		customerRepository.save(customer);
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean customerWithDuplicateEmail(String email) {
		return customerRepository.existsCustomerByEmail(email);
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DeleteCustomer(Integer id) {
		customerRepository.deleteById(id);
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean existCustomerById(Integer id) {
		return customerRepository.existsCustomerById(id);
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateCustomer(Customer customer) {
		customerRepository.save(customer);
		// TODO Auto-generated method stub
		
	}


	

}
