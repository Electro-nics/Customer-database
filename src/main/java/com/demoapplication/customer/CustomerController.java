package com.demoapplication.customer;
// APIs of this applications are handled from here
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController 
@RequestMapping("api/v1/customer")
public class CustomerController {
	private final CustomerService customerService;
	public CustomerController(CustomerService customerService) {
		this.customerService = customerService;
	}
	/*
	@RequestMapping(
			path="api/v1/customer",
			method = RequestMethod.GET
			)
	*/
	@GetMapping
	public List<Customer> getCustomerDetails(){
		return customerService.getAllCustomers();
	}
	@GetMapping("{customerid}")
	public Customer getIndivisualData(@PathVariable("customerid") Integer customerId) {
		return customerService.getCustomersById(customerId);
		
	}
	@PostMapping
	public void insertCustomerDetails(@RequestBody CustomerRegistrationRequest registration) {
		customerService.customerRegistration(registration);
	}
	@DeleteMapping("{customerid}")
	public void deleteRequestedCustomerById(@PathVariable("customerid")Integer customerId) {
		customerService.deleteCustomerById(customerId);
		
	}
	@PutMapping("{customerid}")
	public void updateRequestedCustomer(@PathVariable("customerid") Integer customerId,
				@RequestBody CustomerUpdateRequest updaterequest) {
		customerService.updateCustomer(updaterequest, customerId);
	}
	

}
