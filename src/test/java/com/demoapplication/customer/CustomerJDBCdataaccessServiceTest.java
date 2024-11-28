package com.demoapplication.customer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.filter;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import com.demoapplication.AbstractTestContainer;

public class CustomerJDBCdataaccessServiceTest extends AbstractTestContainer{
	private CustomerJDBCdataaccessService underTest;
	private final CustomerRawMapper customerRawMapper= new CustomerRawMapper();
	
	@BeforeEach
	void setup() {
		underTest = new CustomerJDBCdataaccessService(
				getJdbcTemplate(),
				customerRawMapper
				);
		
	}
	@Test
	void selectAllCustomers() {
		Customer customer= new Customer(
				fake.name().firstName(),
				fake.internet().safeEmailAddress()+"-"+UUID.randomUUID(),
				20
				);
		underTest.insertCustomer(customer);
		List<Customer> customers= underTest.selectAllCustomers();
		assertThat(customers).isNotEmpty();
		
		
	}
	@Test
	void selectCustomerById() {
		String email =fake.internet().safeEmailAddress()+"-"+UUID.randomUUID();
		Customer customer= new Customer(
				fake.name().firstName(),
				email,
				20
				);
		underTest.insertCustomer(customer);
		int id =underTest.selectAllCustomers()
				.stream().filter(c->c.getEmail().equals(email))
				.map(c->c.getId())
				.findFirst()
				.orElseThrow();
		Optional<Customer> actualCustomer= underTest.selectCustomersById(id);
		
		assertThat(actualCustomer).isPresent().hasValueSatisfying(c->{
			 assertThat(c.getId()).isEqualTo(id);
			 assertThat(c.getName()).isEqualTo(customer.getName());
			 assertThat(c.getEmail()).isEqualTo(customer.getEmail());
			 assertThat(c.getAge()).isEqualTo(customer.getAge());
		});
		
	}
	@Test
	void willReturnEmptyWhenSelectCustomerById() {
		int id =-1;
		var actualCustomer=underTest.selectCustomersById(id);
		assertThat(actualCustomer).isEmpty();
		
	}
	@Test
	void customerWithDuplicateEmail() {
		String email =fake.internet().safeEmailAddress()+"-"+UUID.randomUUID();
		Customer customer= new Customer(
				fake.name().firstName(),
				email,
				20
				);
		underTest.insertCustomer(customer);
	boolean actual=	underTest.customerWithDuplicateEmail(email);
	assertThat(actual).isTrue();
		
		
	}
	@Test
	void customerWithDuplicateEmailDoesnotExits() {
		String email=fake.internet().safeEmailAddress()+"-"+UUID.randomUUID();
		boolean actual=underTest.customerWithDuplicateEmail(email);
		assertThat(actual).isFalse();
		
	}
	@Test
	void existCustomerById() {
		String email =fake.internet().safeEmailAddress()+"-"+UUID.randomUUID();
		Customer customer= new Customer(
				fake.name().firstName(),
				email,
				20
				);
		underTest.insertCustomer(customer);
		int id = underTest.selectAllCustomers()
				.stream()
				.filter(c->c.getEmail()
						.equals(email))
				.map(c->c.getId())
				.findFirst()
				.orElseThrow();
		boolean actual=underTest.existCustomerById(id);
		assertThat(actual).isTrue();
		
	}
	@Test
	void customerWithIdDoesNotExists() {
		int id=-1;
		boolean actual= underTest.existCustomerById(id);
		assertThat(actual).isFalse();
		
	}
	@Test
	void DeleteCustomer() {
		String email =fake.internet().safeEmailAddress()+"-"+UUID.randomUUID();
		Customer customer= new Customer(
				fake.name().firstName(),
				email,
				20
				);
		underTest.insertCustomer(customer);
		int id=underTest.selectAllCustomers().stream().filter(c->c.getEmail().equals(email)).map(c->c.getId()).findFirst().orElseThrow();
		
		underTest.DeleteCustomer(id);
		var actual= underTest.selectCustomersById(id);
		assertThat(actual).isNotPresent();
	}

	void updateCustomerName() {
		String email =fake.internet().safeEmailAddress()+"-"+UUID.randomUUID();
		Customer customer= new Customer(
				fake.name().firstName(),
				email,
				20
				);
		int id = underTest.selectAllCustomers()
		.stream()
		.filter(c->c.getEmail().equals(email))
		.map(c->c.getId())
		.findFirst()
		.orElseThrow();
		String name="Xertyutr";
		
		Customer update = new Customer();
		update.setId(id);
		update.setName(name);
		underTest.updateCustomer(update);
		
		Optional<Customer> actual= underTest.selectCustomersById(id);
		assertThat(actual).isPresent().hasValueSatisfying(c->{
			assertThat(c.getId()).isEqualTo(id);
			assertThat(c.getName()).isEqualTo(name);
			assertThat(c.getEmail()).isEqualTo(customer.getEmail());
			assertThat(c.getAge()).isEqualTo(customer.getAge());
			});
		
	}
	// @Test
	void updateCustomerEmail() {
		String email =fake.internet().safeEmailAddress()+"-"+UUID.randomUUID();
		Customer customer= new Customer(
				fake.name().firstName(),
				email,
				20
				);
		int id = underTest.selectAllCustomers()
		.stream()
		.filter(c->c.getEmail().equals(email))
		.map(c->c.getId())
		.findFirst()
		.orElseThrow();
		var updatedEmail="abc@example.com";
		Customer update= new Customer();
		update.setId(id);
		update.setEmail(updatedEmail);
		underTest.updateCustomer(update);
		
		Optional<Customer> actual= underTest.selectCustomersById(id);
		assertThat(actual).isPresent().hasValueSatisfying(c->{
			assertThat(c.getId()).isEqualTo(id);
			assertThat(c.getName()).isEqualTo(customer.getName());
			assertThat(c.getEmail()).isEqualTo(updatedEmail);
			assertThat(c.getAge(
					
					
					)).isEqualTo(customer.getAge());
		});
	}
	
	void updateCustomerAge() {
		String email =fake.internet().safeEmailAddress()+"-"+UUID.randomUUID();
		Customer customer= new Customer(
				fake.name().firstName(),
				email,
				20
				);
		int id = underTest.selectAllCustomers()
		.stream()
		.filter(c->c.getEmail().equals(email))
		.map(c->c.getId())
		.findFirst()
		.orElseThrow();
		
		int updatedAge = 33;
		
		Customer update= new Customer();
		update.setId(id);
		update.setAge(updatedAge);
		underTest.updateCustomer(update);
		Optional<Customer> actual = underTest.selectCustomersById(id);
		assertThat(actual).isPresent().hasValueSatisfying(c->{
			assertThat(c.getId()).isEqualTo(id);
			assertThat(c.getName()).isEqualTo(customer.getName());
			assertThat(c.getEmail()).isEqualTo(customer.getEmail());
			assertThat(c.getAge()).isEqualTo(updatedAge);
		});
		
		
		
	}
	

	
}
