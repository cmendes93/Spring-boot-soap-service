package com.cmendes.soap.webservice.customersadministration.soap;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.cmendes.soap.webservice.customersadministration.bean.Customer;
import com.cmendes.soap.webservice.customersadministration.service.CustomerDetailService;
import com.cmendes.soap.webservice.customersadministration.soap.Exception.CustomerNotFoundException;

import br.com.cmendes.CustomerDetail;
import br.com.cmendes.DeleteCustomerRequest;
import br.com.cmendes.DeleteCustomerResponse;
import br.com.cmendes.GetAllCustomerDetailRequest;
import br.com.cmendes.GetAllCustomerDetailResponse;
import br.com.cmendes.GetCustomerDetailRequest;
import br.com.cmendes.GetCustomerDetailResponse;
import br.com.cmendes.Status;

@Endpoint
public class CustomerDetailEndPoint {
	
	@Autowired
	CustomerDetailService service;
	
	@PayloadRoot(namespace="http://cmendes.com.br", localPart="GetCustomerDetailRequest")
	@ResponsePayload
	public GetCustomerDetailResponse processCustomerDetailRequest(@RequestPayload GetCustomerDetailRequest req) throws Exception {
		
		Customer customer = service.findByid(req.getId());
		if(customer == null)
			throw new CustomerNotFoundException("Invalid Customer id: "+ req.getId());
		
		return convertToGetCustomerDetailResponse(customer);
	}
	
	private GetCustomerDetailResponse convertToGetCustomerDetailResponse(Customer customer) {
		GetCustomerDetailResponse resp = new GetCustomerDetailResponse();
		resp.setCustomerDetail(convertToCustomerDetail(customer));
		return resp;
	}
	
	private CustomerDetail convertToCustomerDetail(Customer customer) {
		CustomerDetail customerDetail = new CustomerDetail();
		customerDetail.setId(customer.getId());
		customerDetail.setName(customer.getName());
		customerDetail.setPhone(customer.getPhone());
		customerDetail.setEmail(customer.getEmail());
		return customerDetail;
	}
															   
	@PayloadRoot(namespace="http://cmendes.com.br", localPart="GetAllCustomerDetailRequest")
	@ResponsePayload
	public GetAllCustomerDetailResponse processGetAllCustomerDetailRequest(@RequestPayload GetAllCustomerDetailRequest req) throws Exception {
		List<Customer> customers = service.findAll();
		
		return convertToGetAllCustomerDetailResponse(customers);
	}
	
	private GetAllCustomerDetailResponse convertToGetAllCustomerDetailResponse(List<Customer> customers) {
		GetAllCustomerDetailResponse resp = new GetAllCustomerDetailResponse();
		
		customers.stream().forEach(c -> resp.getCustomerDetail().add(convertToCustomerDetail(c)));
		return resp;
	}
																
	@PayloadRoot(namespace="http://cmendes.com.br", localPart="DeleteCustomerRequest")
	@ResponsePayload
	public DeleteCustomerResponse deleteCustomerRequest(@RequestPayload DeleteCustomerRequest req) throws Exception {
		DeleteCustomerResponse resp = new DeleteCustomerResponse();
		resp.setStatus(convertStatusSoap(service.deleteByid(req.getId())));
		return resp;
	}

	
	private Status convertStatusSoap(com.cmendes.soap.webservice.customersadministration.helper.Status statusService) {
		if(statusService == com.cmendes.soap.webservice.customersadministration.helper.Status.FAILURE)
			return br.com.cmendes.Status.FAILURE;
		
		return br.com.cmendes.Status.SUCESS;
	}
}
