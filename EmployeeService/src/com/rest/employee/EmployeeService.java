/**
 * Implementing a RESTful web service
 * All services can return a response instead which would be more useful
 */
package com.rest.employee;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.rest.employee.model.Employee;

@Path("/emp")
public class EmployeeService {

	/**
	 * empID taken from the URL and used as the param.
	 * 
	 * @param empID
	 * @return
	 */
	@GET
	@Path("/get/{empID}")
	@Produces(MediaType.APPLICATION_XML)
	public Employee getEmployee(@PathParam("empID") String empID) {
		// dummy employee created to be returned
		Employee employee = new Employee();
		employee.setEmpId(empID);
		employee.setName("Ian Lodovica");
		employee.setEmail("ian.lodovica@yahoo.com");
		return employee;
	}

	@POST
	@Path("/create")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	public Employee createEmployee(Employee employee) {
		// create logic here
		return employee;
	}

	@PUT
	@Path("/update")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	public Employee updateEmployee(Employee employee) {
		//update logic here
		return employee;
	}
	
	@DELETE
	@Path("/delete/{empID}")
	public Response deleteEmployee(@PathParam("empID") String empID){
		//delete logic
		return Response.ok("Employee with " + empID + "was deleted successfully").build();
	}
}
