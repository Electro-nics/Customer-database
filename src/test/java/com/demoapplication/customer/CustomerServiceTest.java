package com.demoapplication.customer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.demoapplication.customer.exceptions.DuplicateResourceException;
import com.demoapplication.customer.exceptions.RequestValidationException;
import com.demoapplication.customer.exceptions.ResourseNotFound;
@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {
	private CustomerService underTest;
	@Mock
	private CustomerDao customerDao;
	@BeforeEach
	void setup() {
		underTest= new CustomerService(customerDao);
	}
	@Test
	void getAllCustomers() {
		// When
		underTest.getAllCustomers();
//		Then
		verify(customerDao).selectAllCustomers();
		
	}
	@Test
	void getCustomersById() {
		// Given 
		int id=10;
		Customer customer= new Customer(id,"Alex","alex.k@example.com",20);
		when(customerDao.selectCustomersById(id)).thenReturn(Optional.of(customer));
//		When
		Customer actual=underTest.getCustomersById(id);
//		Then
		assertThat(actual).isEqualTo(customer);
	}
	@Test
	void getCustomerByIdErrorHandeler() {
		int id=10;
		when(customerDao.selectCustomersById(id)).thenReturn(Optional.empty());
		assertThatThrownBy(()->underTest
				.getCustomersById(id))
		.isInstanceOf(ResourseNotFound.class)
		.hasMessageContaining("Customer with id [%s] is not found".formatted(id));
	}
	@Test
	 void customerRegistration() {
//		Given
		String email="alex.k@example.com";
		when(customerDao.customerWithDuplicateEmail(email)).thenReturn(false);
//		When
		CustomerRegistrationRequest request=
				new CustomerRegistrationRequest("Alex","alex.k@example.com",23);
		underTest.customerRegistration(request);
//		Then
		ArgumentCaptor<Customer> customerArgumentCaptor= ArgumentCaptor.forClass(Customer.class);
		verify(customerDao).insertCustomer(customerArgumentCaptor.capture());
		Customer capturedCustomer=customerArgumentCaptor.getValue();
		assertThat(capturedCustomer.getId()).isNull();
		assertThat(capturedCustomer.getName()).isEqualTo(request.name());
		assertThat(capturedCustomer.getEmail()).isEqualTo(request.email());
		assertThat(capturedCustomer.getAge()).isEqualTo(request.age());
		
		 
	 }
	@Test
	void customerRegistrationDuplicateEmail() {
		String email="abc@example.com";
		when(customerDao.customerWithDuplicateEmail(email)).thenReturn(true);
		CustomerRegistrationRequest request=
				new CustomerRegistrationRequest("Alex",email,23);
		assertThatThrownBy(()->underTest
				.customerRegistration(request))
		.isInstanceOf(DuplicateResourceException.class)
		.hasMessage("Email is already in use");
		verify(customerDao,never()).insertCustomer(any());	
	}
	@Test
	void deleteCustomerById() {
//		Given
		int id = 5;
		when(customerDao.existCustomerById(id)).thenReturn(true);
//		When
		underTest.deleteCustomerById(id);
		//Then
		verify(customerDao).DeleteCustomer(id);	
	}
	@Test
	void deleteCustomerByIdErrorHandeler() {
//		Given
		int id=5;
		when(customerDao.existCustomerById(id)).thenReturn(false);
//		When
		assertThatThrownBy(()->underTest
				.deleteCustomerById(id))
		.isInstanceOf(ResourseNotFound.class)
		.hasMessage("Customer with id [%s] is not available".formatted(id));
//		Then
		
		verify(customerDao,never()).DeleteCustomer(any());
		
	}
	@Test
	void updateAllCustomerFields() {
//		Given
		int id=10;
		Customer customer= new Customer(id,"Alex","alex.k@example.com",20);
		when(customerDao.selectCustomersById(id)).thenReturn(Optional.of(customer));
		String newEmail="alexdrabo@example.com";
		when(customerDao.customerWithDuplicateEmail(newEmail)).thenReturn(false);
		CustomerUpdateRequest update= new CustomerUpdateRequest("Alexdeabo", newEmail, 23);
//		When
		underTest.updateCustomer(update, id);
//		Then
		ArgumentCaptor<Customer> updateRequestCaptor= ArgumentCaptor.forClass(Customer.class);
		
		verify(customerDao).updateCustomer(updateRequestCaptor.capture());
		
		Customer updatedCustomerValue= updateRequestCaptor.getValue();
		
		assertThat(updatedCustomerValue.getName()).isEqualTo(update.name());
		assertThat(updatedCustomerValue.getEmail()).isEqualTo(update.email());
		assertThat(updatedCustomerValue.getAge()).isEqualTo(update.age());
		
	}@Test
	void updateCustomerNameOnly() {
		int id=10;
		Customer customer= new Customer(id,"Alex","alex.k@example.com",20);
		when(customerDao.selectCustomersById(id)).thenReturn(Optional.of(customer));
		String newName="Ridex";

		CustomerUpdateRequest update= new CustomerUpdateRequest(newName,"alex.k@example.com",20);
//		When
		underTest.updateCustomer(update, id);
//		Then
		ArgumentCaptor<Customer> updateRequestCaptor= ArgumentCaptor.forClass(Customer.class);
		
		verify(customerDao).updateCustomer(updateRequestCaptor.capture());
		
		Customer updatedCustomerValue= updateRequestCaptor.getValue();
		
		assertThat(updatedCustomerValue.getName()).isEqualTo(update.name());
		assertThat(updatedCustomerValue.getEmail()).isEqualTo(customer.getEmail());
		assertThat(updatedCustomerValue.getAge()).isEqualTo(customer.getAge());
		
	}
	@Test
	void updateCustomerEmailOnly() {
//		Given
		int id=10;
		Customer customer= new Customer(id,"Alex","alex.k@example.com",20);
		when(customerDao.selectCustomersById(id)).thenReturn(Optional.of(customer));
		String newEmail="alexdrabo@example.com";
		when(customerDao.customerWithDuplicateEmail(newEmail)).thenReturn(false);
		CustomerUpdateRequest update= new CustomerUpdateRequest("Alex", newEmail, 20);
//		When
		underTest.updateCustomer(update, id);
//		Then
		ArgumentCaptor<Customer> updateRequestCaptor= ArgumentCaptor.forClass(Customer.class);
		
		verify(customerDao).updateCustomer(updateRequestCaptor.capture());
		
		Customer updatedCustomerValue= updateRequestCaptor.getValue();
		
		assertThat(updatedCustomerValue.getName()).isEqualTo(customer.getName());
		assertThat(updatedCustomerValue.getEmail()).isEqualTo(update.email());
		assertThat(updatedCustomerValue.getAge()).isEqualTo(customer.getAge());
		
	}
	@Test
	void updateCustomerAgeOnly() {
//		Given
		int id=10;
		Customer customer= new Customer(id,"Alex","alex.k@example.com",20);
		when(customerDao.selectCustomersById(id)).thenReturn(Optional.of(customer));
		int newAge=50;
		CustomerUpdateRequest update= new CustomerUpdateRequest("Alex","alex.k@example.com", newAge);
//		When
		underTest.updateCustomer(update, id);
//		Then
		ArgumentCaptor<Customer> updateRequestCaptor= ArgumentCaptor.forClass(Customer.class);
		
		verify(customerDao).updateCustomer(updateRequestCaptor.capture());
		
		Customer updatedCustomerValue= updateRequestCaptor.getValue();
		
		assertThat(updatedCustomerValue.getName()).isEqualTo(customer.getName());
		assertThat(updatedCustomerValue.getEmail()).isEqualTo(customer.getEmail());
		assertThat(updatedCustomerValue.getAge()).isEqualTo(update.age());
		
	}
	@Test
	void updateCustomerDuplicateEmailId() {
		int id=10;
		String email="abc@example.com";
		Customer customer= new Customer(id,"Alex",email,20);
		when(customerDao.selectCustomersById(id)).thenReturn(Optional.of(customer));
		String newEmail="alexzender@example.com";
		when(customerDao.customerWithDuplicateEmail(newEmail)).thenReturn(true);
		
		CustomerUpdateRequest update= new CustomerUpdateRequest(null, newEmail, null);
		
		assertThatThrownBy(()->underTest.updateCustomer(update, id))
		.isInstanceOf(DuplicateResourceException.class)
		.hasMessage("Enmail is Already taken");
		verify(customerDao,never()).updateCustomer(any());
		
	}
	@Test
	void updateCustomerWithNoFieldUpdated() {
		int id=10;
		String email="abc@example.com";
		Customer customer= new Customer(id,"Alex",email,20);
		when(customerDao.selectCustomersById(id)).thenReturn(Optional.of(customer));
		CustomerUpdateRequest update= new CustomerUpdateRequest(customer.getName(),customer.getEmail(),customer.getAge());
		assertThatThrownBy(()->underTest.updateCustomer(update, id))
		.isInstanceOf(RequestValidationException.class)
		.hasMessage("No changes reflacted !!");
		verify(customerDao,never()).updateCustomer(any());
	}
	
}
