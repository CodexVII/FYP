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
import javax.ws.rs.client.WebTarget;
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
	@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response register(@FormParam("name") String username, @FormParam("password") String password) {
		ServiceAccessCounter.incrementRegisterCount();
		if (username != null && password != null && !username.isEmpty() && !password.isEmpty()) {
			User user = new User();
			user.setUsername(username);
			user.hashPassword(password);
			userEJB.saveUser(user);

			// get the reference from DB for new details
			Usergroup up = new Usergroup();
			up.setUsername(username);
			up.setDomain("admin");
			upEJB.save(up);

			return Response.ok("Registration success").build();
		}

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

				return Response.ok("Deletion success").build();
			}
		} catch (Exception e) {
			System.out.println("Caught NoResultException");
			return Response.status(404).entity("User not found").build();
		}

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
	@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
	public Response get(@PathParam("user") String username) {
		ServiceAccessCounter.incrementGetCount();

		if (username != null && !username.isEmpty()) {
			try {
				User user = new User();
				user = userEJB.getUser(username);
				return Response.ok(user).build();
			} catch (Exception e) {
				System.out.println("Caught NoResultException");
				return Response.status(404).entity("User not found").build();
			}
		}
		return Response.ok("No username provided").build();
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
//	@Produces(MediaType.APPLICATION_JSON)
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
				return Response.ok("Password update success").build();
			}
			return Response.ok("Username or Password was incorrect").build();
		} catch (Exception e) {
			System.out.println("Caught NoResultException");
//			ObjectNode payload = objectMapper.createObjectNode();
//			payload.put("error", "User not found");
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

		if (pattern != null && !pattern.isEmpty()) {
			List<User> result = userEJB.patternSearch(pattern);

			// get a generic entity else
			// Severe: MessageBodyWriter not found for media
			// type=application/json, type=class
			// java.util.Vector, genericType=class java.util.Vector. error
			GenericEntity<List<User>> entity = new GenericEntity<List<User>>(result) {
			};
			return Response.ok(entity).build();
		}
		return Response.ok("Please enter a search pattern").build();
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

		Client client = ClientBuilder.newClient(); // create REST client inside
													// service
		ObjectMapper objectMapper = new ObjectMapper(); // used to extract JSON
														// data to user object

		// Get user from the DB
		if (username != null && password != null && !username.isEmpty() && !password.isEmpty()) {
			// get sender
			WebTarget webTarget = client.target(API).path("get").path(username);
			Response response = webTarget.request(MediaType.APPLICATION_JSON).get();
			String result = response.readEntity(String.class);
			User user = objectMapper.readValue(result, User.class);

			// check if provided password matches with DB one
			if (user.isValid() && user.getPassword().equals(user.generateHash(password))) {
				return Response.ok("Login success").build();
			} else
				return Response.ok("Username or password incorrect").build();
		}
		return Response.ok("Please enter username and password").build();
	}
}
