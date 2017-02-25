/**
 * EJB which also acts as the REST API
 */
package service;

import java.io.IOException;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ejb.UserEJB;
import ejb.UsergroupEJB;
import entity.User;
import entity.Usergroup;
import utility.ServiceAccessCounter;

@Path("/user")
@Stateless
public class UserService {
	private static final String API = "http://localhost/RestApp/rest/user/";
	private static final String MONITOR_API = "http://localhost/APIMonitorService/rest/monitoring";

	private Client client = ClientBuilder.newClient(); // create REST client
														// inside
	// service
	private ObjectMapper objectMapper = new ObjectMapper(); // used to extract
															// JSON
	// data to user object
	@Inject
	UserEJB userEJB;
	@Inject
	UsergroupEJB upEJB;

	/**
	 * Add user to the database. Automatically adds the users to the admin group
	 * 
	 * @param user
	 * @return
	 */
	@POST
	@Path("/add")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response register(@FormParam("name") String username, @FormParam("password") String password) {
		ServiceAccessCounter.incrementRegisterCount();
		if (username != null && password != null && !username.isEmpty() && !password.isEmpty()) {
			// check if user exists
			if (userEJB.patternSearch(username).size() == 0) {
				System.out.println("No result, it's okay");
				// user does not exist, make a new one
				User user = new User();
				user.setUsername(username);
				user.hashPassword(password);
				userEJB.saveUser(user);

				// get the reference from DB for new details
				Usergroup up = new Usergroup();
				up.setUsername(username);
				up.setDomain("admin");
				upEJB.save(up);

				logServicePass("add");
				return Response.ok("Registration success").build();
			} else {
				logServiceFail("add");
				return Response.ok("User exists already").build();
			}
		}

		logServiceFail("add");
		return Response.ok("Please enter username and password").build();
	}

	@POST
	@Path("/delete")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@FormParam("name") String username) {
		ServiceAccessCounter.incrementDeleteCount();
		try {
			User user = userEJB.getUser(username);

			// check to see if the user retrieved exists in the DB.
			if (user.isValid()) {
				// First delete the user from the User table
				userEJB.deleteUser(user);

				logServicePass("delete");
				return Response.ok("Deletion success").build();
			}
		} catch (Exception e) {
			logServiceFail("delete");
			return Response.status(404).entity("User not found").build();
		}
		logServiceFail("delete");
		return Response.ok("No user provided").build();
	}

	/**
	 * @QueryParam equivalent to Spring's @RequestParam. Uses URI query
	 *             attributes like normal.
	 * 
	 * @param username
	 * @return
	 */
	@GET
	@Path("/get/{user}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
	public Response get(@PathParam("user") String username) {
		ServiceAccessCounter.incrementGetCount();

		try {
			User user = new User();
			user = userEJB.getUser(username);

			logServicePass("get");
			return Response.ok(user).build();
		} catch (Exception e) {
			logServiceFail("get");
			return Response.status(404).entity("User not found").build();
		}
	}

	/**
	 * Find user Do a check on the password If valid, update else return error
	 * Get User object from DB Get current password
	 * 
	 * Compare current password with entered password If yes, then update the
	 * password with the new password
	 * 
	 * @param username
	 * @return
	 */
	@POST
	@Path("/update/password")
	// @Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response changePassword(@FormParam("name") String username, @FormParam("old_pwd") String old_password,
			@FormParam("new_pwd") String new_password) {
		ServiceAccessCounter.incrementChangePasswordCount();

		try {
			User user = userEJB.getUser(username);
			String old_pwd = user.getPassword();

			// check if provided password is same as old
			// if true set new password
			if (user.isValid() && user.generateHash(old_password).equals(old_pwd)) {
				user.hashPassword(new_password);
				userEJB.saveUser(user);
				logServicePass("update password");
				return Response.ok("Password update success").build();
			}
			logServiceFail("update password");
			return Response.ok("Username or Password was incorrect").build();
		} catch (Exception e) {
			logServiceFail("update password");
			return Response.status(404).entity("User not found").build();
		}

	}

	/**
	 * Using search term get list of users with that pattern Used to
	 * use @QueryParam
	 * 
	 * @param term
	 * @return
	 */
	@GET
	@Path("/search/{pattern}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response search(@PathParam("pattern") String pattern) {
		// update request count
		ServiceAccessCounter.incrementSearchCount();
		// pattern will never be empty

		List<User> result = userEJB.patternSearch(pattern);

		// get a generic entity else
		// Severe: MessageBodyWriter not found for media
		// type=application/json, type=class
		// java.util.Vector, genericType=class java.util.Vector. error
		GenericEntity<List<User>> entity = new GenericEntity<List<User>>(result) {
		};
		logServicePass("search");
		return Response.ok(entity).build();
	}

	/**
	 * Verifies username + password with one stored in DB
	 * 
	 * Get user from db with matching username Run a SHA-256 hash on password
	 * provided and see if it matches with hash in DB
	 * 
	 * Return whether or not it matches
	 * 
	 * @param username
	 * @param password
	 * @return
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	@POST
	@Path("/validate")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response validate(@FormParam("name") String username, @FormParam("password") String password)
			throws JsonParseException, JsonMappingException, IOException {
		ServiceAccessCounter.incrementValidateCount();

		// Get user from the DB
		if (username != null && password != null && !username.isEmpty() && !password.isEmpty()) {
			// get sender
			WebTarget webTarget = client.target(API).path("get").path(username);
			Response response = webTarget.request(MediaType.APPLICATION_JSON).get();
			String result = response.readEntity(String.class);
			User user = objectMapper.readValue(result, User.class);

			// check if provided password matches with DB one
			if (user.isValid() && user.getPassword().equals(user.generateHash(password))) {
				logServicePass("validate");
				return Response.ok("Login success").build();
			} else
				logServiceFail("validate");
			return Response.ok("Username or password incorrect").build();
		}
		logServiceFail("validate");
		return Response.ok("Please enter username and password").build();
	}

	private void logServicePass(String operation) {
		WebTarget webTarget = client.target(MONITOR_API).path("log").path("pass");

		Form form = new Form();
		form.param("service", "user"); // unique for each service
		form.param("operation", operation);

		webTarget.request(MediaType.APPLICATION_JSON).post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED),
				Response.class);
	}

	private void logServiceFail(String operation) {
		WebTarget webTarget = client.target(MONITOR_API).path("log").path("fail");

		Form form = new Form();
		form.param("service", "user"); // unique for each service
		form.param("operation", operation);

		webTarget.request(MediaType.APPLICATION_JSON).post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED),
				Response.class);
	}
}
