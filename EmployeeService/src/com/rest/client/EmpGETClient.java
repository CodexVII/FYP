package com.rest.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.rest.employee.model.Employee;

public class EmpGETClient {
	public static void main(String[] args){
		Client client = ClientBuilder.newClient();
		WebTarget webTarget = client.target("http://www.myapp.com:8080/EmployeeService/rest/emp/get/100");
		
		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_XML);
		Response response = invocationBuilder.get();
		
		Employee employee = response.readEntity(Employee.class);
		
		System.out.println(response.getStatus());
		System.out.println(employee);
	}
}
