package com.demoapplication.customer;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;

import com.demoapplication.AbstractTestContainer;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CustomerRepositoryTest extends AbstractTestContainer{
	@Autowired
	private CustomerRepository underTest;
	@Autowired
	private ApplicationContext applicationContext;
	
	
	@BeforeEach
	void setup() {
		
	}
	@Test
	void existsCustomerByEmail() {
		String email =fake.internet().safeEmailAddress()+"-"+UUID.randomUUID();
		Customer customer= new Customer(
				fake.name().firstName(),
				email,
				20
				);
		underTest.save(customer);
		boolean actual=underTest.existsCustomerByEmail(email);
		assertThat(actual).isTrue();
		
	}
	@Test
	void deleteById() {
		String email =fake.internet().safeEmailAddress()+"-"+UUID.randomUUID();
		Customer customer= new Customer(
				fake.name().firstName(),
				email,
				20
				);
		underTest.save(customer);
		int id = underTest.findAll()
				.stream()
				.filter(c->c.getEmail()
						.equals(email))
				.map(c->c.getId())
				.findFirst()
				.orElseThrow();
		underTest.deleteById(id);
		var actual=underTest.findById(id);
		assertThat(actual).isNotPresent();
		
	}
	@Test
	void existsCustomerById() {
		String email =fake.internet().safeEmailAddress()+"-"+UUID.randomUUID();
		Customer customer= new Customer(
				fake.name().firstName(),
				email,
				20
				);
		underTest.save(customer);
		int id = underTest.findAll()
				.stream()
				.filter(c->c.getEmail()
						.equals(email))
				.map(c->c.getId())
				.findFirst()
				.orElseThrow();
		boolean actual=underTest.existsById(id);
		assertThat(actual).isTrue();
	}

}
