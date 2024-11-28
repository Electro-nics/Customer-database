package com.demoapplication;

import java.util.List;
import java.util.Random;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.demoapplication.customer.Customer;
import com.demoapplication.customer.CustomerRepository;
import com.github.javafaker.Faker;


@SpringBootApplication
public class Main {
	public static void main(String[] args) {
		
		SpringApplication.run(Main.class, args);
		// TODO Auto-generated method stub

	}
	@Bean
	CommandLineRunner runner(CustomerRepository customerRepository) {
		
		return args->{
			Faker faker= new Faker();
			String firstName=faker.name().firstName();
			String lastName=faker.name().lastName();
			Random random=new Random();
			
			Customer customer=new Customer(
					firstName+" "+lastName,
					firstName.toString().toLowerCase()+"."+lastName.toString().toLowerCase()+"@yourdomain.com",
					random.nextInt(16,99)
					);
			List<Customer> customers= List.of(customer);
			customerRepository.saveAll(customers);
		};
	}

}
