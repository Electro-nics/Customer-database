package com.demoapplication.customer;
// This interface is implemented in CustomerAccessService class which is responsible for database of the application
import java.util.List;
import java.util.Optional;

public interface CustomerDao {
	List<Customer> selectAllCustomers();
	Optional<Customer> selectCustomersById(Integer id);
	void insertCustomer(Customer customer);
	boolean customerWithDuplicateEmail(String email);
	void DeleteCustomer(Integer id);
	boolean existCustomerById(Integer id);
	void updateCustomer(Customer customer);

}
