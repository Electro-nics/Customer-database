package com.demoapplication.journey;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.demoapplication.customer.Customer;
import com.demoapplication.customer.CustomerRegistrationRequest;
import com.demoapplication.customer.CustomerUpdateRequest;
import com.github.javafaker.Faker;

import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CustomerIntegrationTest {
	@Autowired
	private WebTestClient webTestClient;
	private static final Random randomNumber= new Random();
	private static final String CUSTOMER_URI="api/v1/customer";
	//@Test
	void canRegisterACustomer() {
		Faker faker = new Faker();
		String name= faker.name().name();
		String email= faker.name().firstName()+ UUID.randomUUID()+"example.com";
		int age= randomNumber.nextInt(1, 100);
		// Create a registration request
		CustomerRegistrationRequest request= new CustomerRegistrationRequest(name, email, age);
		// Send a post request 
		webTestClient.post()
		.uri(CUSTOMER_URI)
		.accept(MediaType.APPLICATION_JSON)
		.contentType(MediaType.APPLICATION_JSON)
		.body(Mono.just(request),CustomerRegistrationRequest.class)
		.exchange()
		.expectStatus()
		.isOk();
		
		// Get All customer
		List<Customer> allCustomer= webTestClient.get()
		.uri(CUSTOMER_URI)
		.accept(MediaType.APPLICATION_JSON)
		.exchange()
		.expectStatus()
		.isOk()
		.expectBodyList(new ParameterizedTypeReference<Customer>() {})
		.returnResult()
		.getResponseBody();
		
		// Make sure the customer is present
		Customer expectedCustomer= new Customer(
				name,
				email,
				age
				);
		
		assertThat(allCustomer)
		.usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
		.contains(expectedCustomer);
		
		int id=allCustomer.stream().filter(c->c.getEmail().equals(email))
				.map(c->c.getId())
				.findFirst()
				.orElseThrow();
		expectedCustomer.setId(id);
		
		// get customer By id:
		webTestClient.get()
		.uri(CUSTOMER_URI+ "/{id}",id)
		.accept(MediaType.APPLICATION_JSON)
		.exchange()
		.expectStatus()
		.isOk()
		.expectBody(new ParameterizedTypeReference<Customer>() {})
		.isEqualTo(expectedCustomer);
		}
	//@Test
	void canDeleteACustomer() {
		Faker faker = new Faker();
		String name= faker.name().name();
		String email= faker.name().firstName()+ UUID.randomUUID()+"example.com";
		int age= randomNumber.nextInt(1, 100);
		// Create a registration request
		CustomerRegistrationRequest request= new CustomerRegistrationRequest(name, email, age);
		// Send a post request 
		webTestClient.post()
		.uri(CUSTOMER_URI)
		.accept(MediaType.APPLICATION_JSON)
		.contentType(MediaType.APPLICATION_JSON)
		.body(Mono.just(request),CustomerRegistrationRequest.class)
		.exchange()
		.expectStatus()
		.isOk();
		
		// Get All customer
		List<Customer> allCustomer= webTestClient.get()
		.uri(CUSTOMER_URI)
		.accept(MediaType.APPLICATION_JSON)
		.exchange()
		.expectStatus()
		.isOk()
		.expectBodyList(new ParameterizedTypeReference<Customer>() {})
		.returnResult()
		.getResponseBody();
		
		
		
		int id=allCustomer.stream().filter(c->c.getEmail().equals(email))
				.map(c->c.getId())
				.findFirst()
				.orElseThrow();
		
		// Delete Customer
		webTestClient.delete()
		.uri(CUSTOMER_URI+ "/{id}",id)
		.accept(MediaType.APPLICATION_JSON)
		.exchange()
		.expectStatus()
		.isOk();
		
		
		// get customer By id:
		webTestClient.get()
		.uri(CUSTOMER_URI+ "/{id}",id)
		.accept(MediaType.APPLICATION_JSON)
		.exchange()
		.expectStatus()
		.isNotFound();
	}
	//@Test
	void canUpdateCustomer() {
		Faker faker = new Faker();
		String name= faker.name().name();
		String email= faker.name().firstName()+ UUID.randomUUID()+"example.com";
		int age= randomNumber.nextInt(1, 100);
		// Create a registration request
		CustomerRegistrationRequest request= new CustomerRegistrationRequest(name, email, age);
		// Send a post request 
		webTestClient.post()
		.uri(CUSTOMER_URI)
		.accept(MediaType.APPLICATION_JSON)
		.contentType(MediaType.APPLICATION_JSON)
		.body(Mono.just(request),CustomerRegistrationRequest.class)
		.exchange()
		.expectStatus()
		.isOk();
		
		// Get All customer
		List<Customer> allCustomer= webTestClient.get()
		.uri(CUSTOMER_URI)
		.accept(MediaType.APPLICATION_JSON)
		.exchange()
		.expectStatus()
		.isOk()
		.expectBodyList(new ParameterizedTypeReference<Customer>() {})
		.returnResult()
		.getResponseBody();
		
		
		
		int id=allCustomer.stream().filter(c->c.getEmail().equals(email))
				.map(c->c.getId())
				.findFirst()
				.orElseThrow();
		String newName="Jenny";
		CustomerUpdateRequest update = new CustomerUpdateRequest(newName, email, age);
		
		// Update Customer
		webTestClient.put()
		.uri(CUSTOMER_URI+ "/{id}",id)
		.accept(MediaType.APPLICATION_JSON)
		.contentType(MediaType.APPLICATION_JSON)
		.body(Mono.just(update),CustomerUpdateRequest.class)
		.exchange()
		.expectStatus()
		.isOk();
		
		Customer expectedCustomer= new Customer(newName,email,age);
		
		// get customer By id:
		webTestClient.get()
		.uri(CUSTOMER_URI+ "/{id}",id)
		.accept(MediaType.APPLICATION_JSON)
		.exchange()
		.expectStatus()
		.isOk()
		.expectBody(new ParameterizedTypeReference<Customer>(){})
		.isEqualTo(expectedCustomer);
		
	}
	
	}
