package com.rest.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.rest.employee.model.Employee;

public class EmpPOSTClient {
	public static void main(String[] args){
		Client client = ClientBuilder.newClient();
		WebTarget webTarget = client.target("http://www.myapp.com:8080/EmployeeService/rest/emp/create");
		
		Employee emp = new Employee();
		emp.setName("Marina");
		emp.setEmpId("1");
		emp.setEmail("marina@gmail.com");
		
		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_XML);
		Response response = invocationBuilder.post(Entity.entity(emp, MediaType.APPLICATION_XML));
		
		System.out.println(response.getStatus());
		System.out.println(response.readEntity(String.class));
	}
}
