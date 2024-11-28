package com.demoapplication.customer;
// This is the Business layer of the application also it handles errors 
import java.util.List;

import org.springframework.aop.ThrowsAdvice;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.demoapplication.customer.exceptions.DuplicateResourceException;
import com.demoapplication.customer.exceptions.RequestValidationException;
import com.demoapplication.customer.exceptions.ResourseNotFound;
@Service
public class CustomerService {
	private final CustomerDao customerDao ;
	
	public CustomerService(@Qualifier("jpa") CustomerDao customerDao) {
		this.customerDao = customerDao;
	}

	public List<Customer> getAllCustomers(){
		return customerDao.selectAllCustomers();
	}
	public Customer getCustomersById(Integer id){
		return customerDao.selectCustomersById(id)
				.orElseThrow(
						()-> new ResourseNotFound("Customer with id [%s] is not found".formatted(id))
						);
		
	}
	
	public void customerRegistration(CustomerRegistrationRequest customerRegistrationRequest) {
		String email=customerRegistrationRequest.email();
		if(customerDao.customerWithDuplicateEmail(email)) {
			throw new DuplicateResourceException("Email is already in use");
		}
		// add your customer details
		customerDao.insertCustomer(
				new Customer(
						customerRegistrationRequest.name(),
						customerRegistrationRequest.email(),
						customerRegistrationRequest.age()
						)
				);
		
	}
	void deleteCustomerById(Integer custId) {
		if(! customerDao.existCustomerById(custId)) {
			throw new ResourseNotFound("Customer with id [%s] is not available".formatted(custId));
		}
		customerDao.DeleteCustomer(custId);
	}
	void updateCustomer(CustomerUpdateRequest custUpdate, Integer customerId) {
		Customer customer= getCustomersById(customerId);
		boolean change=false;
		// check name validation
		if(custUpdate.name() !=null && ! custUpdate.name().equals(customer.getName())) {
			customer.setName(custUpdate.name());
			change=true;
		}
		// check email validation
		if(custUpdate.email() !=null && ! custUpdate.email().equals(customer.getEmail())) {
			if(customerDao.customerWithDuplicateEmail(custUpdate.email())) {
				throw new DuplicateResourceException("Enmail is Already taken");
			}
			customer.setEmail(custUpdate.email());
			change= true;
		}
		// check age validation
		if(custUpdate.age() != null && !custUpdate.age().equals(customer.getAge())) {
			customer.setAge(custUpdate.age());
			change=true;
		}
		if(!change) {
			throw new RequestValidationException("No changes reflacted !!");
		}
		customerDao.updateCustomer(customer);
		
		
	}

}
