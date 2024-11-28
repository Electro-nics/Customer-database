package com.demoapplication.customer;

public record CustomerRegistrationRequest(
		String name,
		String email,
		Integer age
		) {

}
