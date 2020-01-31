package com.cmendes.soap.webservice.customersadministration.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EnumType;

import org.springframework.stereotype.Component;

import com.cmendes.soap.webservice.customersadministration.bean.Customer;
import com.cmendes.soap.webservice.customersadministration.helper.Status;

@Component
public class CustomerDetailService {
	
	private static List<Customer> customers = new ArrayList<>();
	
	static {
		Customer customer1 = new Customer(1, "Bob", "99999999", "bob@gmail.com");
		customers.add(customer1);
		
		Customer customer2 = new Customer(2, "Marley", "88888", "marley@gmail.com");
		customers.add(customer2);

		Customer customer3 = new Customer(3, "Luiza", "8546795", "luiza@gmail.com");
		customers.add(customer3);
		
		Customer customer4 = new Customer(4, "Jose", "78546487", "jose@gmail.com");
		customers.add(customer4);

	}
	
	public Customer findByid(int id) {
		Optional<Customer> customerOptional = customers.stream().filter(c -> c.getId() == id).findAny();
		
		if(customerOptional.isPresent())
			return customerOptional.get();
		
		return null;
	}
	
	public List<Customer> findAll(){
		return customers;
	}
	
	public Status deleteByid(int id) {
		Optional<Customer> customerOptional = customers.stream().filter(c -> c.getId() == id).findAny();
		
		if(customerOptional.isPresent()) {
			customers.remove(customerOptional.get());
			return Status.SUCESS;
		}
		
		return Status.FAILURE;
	}
}
