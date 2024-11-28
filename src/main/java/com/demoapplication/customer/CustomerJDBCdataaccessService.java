package com.demoapplication.customer;

import java.util.List;
import java.util.Optional;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
@Repository("jdbc")
public class CustomerJDBCdataaccessService implements CustomerDao {

	private final JdbcTemplate jdbcTemplate;
	private final CustomerRawMapper customerRawMapper;
	public CustomerJDBCdataaccessService(JdbcTemplate jdbcTemplate, CustomerRawMapper customerRawMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.customerRawMapper =customerRawMapper;
	}

	@Override
	public List<Customer> selectAllCustomers() {
		var sql="""
				select id, name,email,age from customer
				""";

		
		return jdbcTemplate.query(sql,customerRawMapper);
		}
	@Override
	public Optional<Customer> selectCustomersById(Integer id) {
		var sql= """
				select id, name, email, age
				from customer 
				where id = ?
				""";
		// TODO Auto-generated method stub
		return jdbcTemplate.query(sql,customerRawMapper,id)
				.stream()
				.findFirst();
	}

	@Override
	public void insertCustomer(Customer customer) {
		var sql="""
				INSERT INTO customer (name,email,age)
				VALUES(?, ?, ?)
				""" ;
		jdbcTemplate.update(
				sql,
				customer.getName(),
				customer.getEmail(),
				customer.getAge()
				);
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean customerWithDuplicateEmail(String email) {
		var sql="""
				select count(id) from customer where email= ?
				""";
		Integer count=jdbcTemplate.queryForObject(sql,Integer.class,email);
		
		return count!=null && count>0;
		
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DeleteCustomer(Integer id) {
		var sql="""
				DELETE FROM customer where id = ?
				 """;
		jdbcTemplate.update(sql,id);
		 
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean existCustomerById(Integer id) {
		var sql="""
				select count(*) from customer where id = ?
				""";
		Integer count=jdbcTemplate.queryForObject(sql, Integer.class,id);
		
		return count!=null && count>0;
		// TODO Auto-generated method stub
	}

	@Override
	public void updateCustomer(Customer customer) {
		if(customer.getName() !=null) {
			
			var sql="""
					update customer set name= ?
					where id= ?
					""" ;
			jdbcTemplate.update(sql,customer.getName(),customer.getId());
		}
		if(customer.getEmail() !=null) {
			var sql="""
					UPDATE customer SET email = ?
					WHERE id= ?
					 """;
			jdbcTemplate.update(sql,customer.getEmail(),customer.getId());
		}
		if(customer.getAge()!=null) {
			var sql="""
					UPDATE customer SET age= ?
					WHERE id= ?
					""";
			jdbcTemplate.update(sql,customer.getAge(),customer.getId());
		}
		
		// TODO Auto-generated method stub
		
	}

}
