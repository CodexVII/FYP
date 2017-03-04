/**
 * EJB which also acts as the REST Constants.API
 */
package service;

import java.io.IOException;
import java.net.InetAddress;
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
import utility.Constants;
import utility.ServiceAccessCounter;

@Path("/user")
@Stateless
public class UserService {
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

				ServiceAccessCounter.servicePass("register");
				return Response.ok("Registration success").build();
			} else {
				ServiceAccessCounter.serviceFail("register");
				return Response.ok("User exists already").build();
			}
		}
		ServiceAccessCounter.serviceFail("register");
		return Response.ok("Please enter username and password").build();
	}

	@POST
	@Path("/delete")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@FormParam("name") String username) {
		try {
			User user = userEJB.getUser(username);

			// check to see if the user retrieved exists in the DB.
			if (user.isValid()) {
				// First delete the user from the User table
				userEJB.deleteUser(user);

				ServiceAccessCounter.servicePass("delete");
				return Response.ok("Deletion success").build();
			}
		} catch (Exception e) {
			ServiceAccessCounter.serviceFail("delete");
			return Response.status(404).entity("User not found").build();
		}
		ServiceAccessCounter.serviceFail("delete");
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
		try {
			User user = new User();
			user = userEJB.getUser(username);

			ServiceAccessCounter.servicePass("get");
			return Response.ok(user).build();
		} catch (Exception e) {
			ServiceAccessCounter.serviceFail("get");
			return Response.ok("User not found").build();
		}

	}

	@GET
	@Path("/get")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
	public Response emptyGet() {
		return Response.ok("Please specify a user").build();

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
	public Response updatePassword(@FormParam("name") String username, @FormParam("old_pwd") String old_password,
			@FormParam("new_pwd") String new_password) {

		try {
			User user = userEJB.getUser(username);
			String old_pwd = user.getPassword();

			// check if provided password is same as old
			// if true set new password
			if (user.isValid() && user.generateHash(old_password).equals(old_pwd)) {
				user.hashPassword(new_password);
				userEJB.saveUser(user);
				ServiceAccessCounter.servicePass("update password");
				return Response.ok("Password update success").build();
			}
			ServiceAccessCounter.serviceFail("update password");
			return Response.ok("Username or Password was incorrect").build();
		} catch (Exception e) {
			ServiceAccessCounter.serviceFail("update password");
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
		// pattern will never be empty
		List<User> result = userEJB.patternSearch(pattern);

		// get a generic entity else
		// Severe: MessageBodyWriter not found for media
		// type=application/json, type=class
		// java.util.Vector, genericType=class java.util.Vector. error
		GenericEntity<List<User>> entity = new GenericEntity<List<User>>(result) {
		};

		// check if empty
		if (result.size() > 0) {
			ServiceAccessCounter.servicePass("search");
			return Response.ok(entity).build();
		} else {
			ServiceAccessCounter.serviceFail("search");
			return Response.ok("No users found").build();
		}
	}

	/**
	 * Convenience service that informs users that to access the service an
	 * input is required.
	 * 
	 * @return
	 */
	@GET
	@Path("/search/")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response emptySearch() {
		// pattern will never be empty
		return Response.ok("Please specify user").build();

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
		// Get user from the DB
		if (username != null && password != null && !username.isEmpty() && !password.isEmpty()) {
			// get sender
			User user;
			try {
				user = userEJB.getUser(username);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				ServiceAccessCounter.serviceFail("validate");
				return Response.ok("User not found").build();
			}

			// check if provided password matches with DB one
			if (user.isValid() && user.getPassword().equals(user.generateHash(password))) {
				ServiceAccessCounter.servicePass("validate");
				return Response.ok("Login success").build();
			} else
				ServiceAccessCounter.serviceFail("validate");
			return Response.ok("Username or password incorrect").build();
		}
		ServiceAccessCounter.serviceFail("validate");
		return Response.ok("Please enter username and password").build();
	}
}
