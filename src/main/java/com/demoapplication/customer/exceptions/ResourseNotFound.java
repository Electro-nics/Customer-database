package com.demoapplication.customer.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code=HttpStatus.NOT_FOUND)
public class ResourseNotFound extends RuntimeException{
	
	public ResourseNotFound(String message) {
		super(message);
	}

}
