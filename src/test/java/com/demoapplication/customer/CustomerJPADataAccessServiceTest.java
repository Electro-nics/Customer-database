package com.demoapplication.customer;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.demoapplication.AbstractTestContainer;

public class CustomerJPADataAccessServiceTest extends AbstractTestContainer {
	@Autowired
	private CustomerJPADataAccessServices underTest;
	
	private AutoCloseable autoCloseable;
	@Mock
	private CustomerRepository customerRepository;
	@BeforeEach
	void setup() {
		autoCloseable=MockitoAnnotations.openMocks(this);
		
		underTest = new CustomerJPADataAccessServices(customerRepository);
	}
	@AfterEach
	void tearDown() throws Exception {
		autoCloseable.close();
	}
	@Test
	void selectAllCustomers() {
		// When 
		underTest.selectAllCustomers();
//		Then
		Mockito.verify(customerRepository).findAll();
		
	}
	@Test
	void selectCustomersById() {
//		Given 
		int id =1;
//		When
		underTest.selectCustomersById(id);
//		Then
		verify(customerRepository).findById(id);
	}
	@Test
	void insertCustomer() {
//		Given
		Customer customer = new Customer("Hervy","hervyjonson@example.com",30);
//		When
		underTest.insertCustomer(customer);
//		Then
		verify(customerRepository).save(customer);
	}
	@Test
	void customerWithDuplicateEmail() {
		// Given
		String email="example@exaple.com";
		// When
		underTest.customerWithDuplicateEmail(email);
		// Then
		verify(customerRepository).existsCustomerByEmail(email);
		
	}
	@Test
	void DeleteCustomer() {
		// Given
		int id= 5;
		//When
		underTest.DeleteCustomer(id);
		// Then
		verify(customerRepository).deleteById(id);
	}
	@Test
	void existCustomerById() {
		// Given
		int id=4;
		//When
		underTest.existCustomerById(id);
		// Then
		verify(customerRepository).existsCustomerById(id);
		
	}
	void updateCustomer() {
		// Given
		Customer customer = new Customer("Hervy","hervyjonson@example.com",30);
		// When
		underTest.updateCustomer(customer);
		// Then
		verify(customerRepository).save(customer);
	}
	

}
