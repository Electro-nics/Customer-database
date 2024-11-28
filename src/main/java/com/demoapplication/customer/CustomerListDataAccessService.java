package com.demoapplication.customer;
 import java.util.ArrayList;
// This is the DataBase of the application and logic for fetching data from DB is written here
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
@Qualifier("list")
public class CustomerListDataAccessService implements CustomerDao {
	
	// db
		public static List<Customer> customers;
		static {
			customers= new ArrayList<>();
			Customer alex=new Customer(1,"Alex","alex@gmail.com",23);
			customers.add(alex);
			Customer Jemila=new Customer(2,"Jemila","jemila@gmail.com",19);
					
			customers.add(Jemila);
		}

	@Override
	public List<Customer> selectAllCustomers() {
		// TODO Auto-generated method stub
		return customers;
	}

	@Override
	public Optional<Customer> selectCustomersById(Integer id) {
		return  customers.stream()
				.filter(c->c.getId().equals(id))
				.findFirst();
				
	}

	@Override
	public void insertCustomer(Customer customer) {
		customers.add(customer);
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean customerWithDuplicateEmail(String email) {
		return customers.stream().anyMatch(c->c.getEmail().equals(email));
		// TODO Auto-generated method stub
	}

	@Override
	public void DeleteCustomer(Integer id) {
		customers.stream().filter(c->c.getId().equals(id)).findFirst().ifPresent(customers::remove);
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean existCustomerById(Integer id) {
		return customers.stream().anyMatch(c->c.getId().equals(id));
		
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateCustomer(Customer customer) {
		// TODO Auto-generated method stub
		
	}



	

}
