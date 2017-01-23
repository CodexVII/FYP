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
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response register(@FormParam("name") String username, @FormParam("password") String password) {
		ServiceAccessCounter.incrementRegisterCount();
		if (username != null && password != null && !username.isEmpty() && !password.isEmpty()) {
			User user = new User();
			user.setUsername(username);
			user.hashPassword(password);
			userEJB.saveUser(user);

			Usergroup up = new Usergroup();
			up.setUsername(username);
			up.setGroupname("admin");
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
		User user = userEJB.getUser(username);
		Usergroup usergroup = upEJB.getUsergroup(username);

		// check to see if the user retrieved exists in the DB.
		if (user.isValid()) {
			// First delete the user from the User table
			userEJB.deleteUser(user);

			// Next delete the user from the Usergroup table
			upEJB.delete(usergroup);

			return Response.ok("Deletion success").build();
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
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("user") String username) {
		ServiceAccessCounter.incrementGetCount();
		System.out.println("get incremented " + username);
		if (username != null && !username.isEmpty()) {
			User user = new User();
			user = userEJB.getUser(username);
			return Response.ok(user).build();
		}
		return Response.ok("Could not find user").build();
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
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response changePassword(@FormParam("name") String username, @FormParam("old_pwd") String old_password,
			@FormParam("new_pwd") String new_password) {
		ServiceAccessCounter.incrementChangePasswordCount();

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
		System.out.println("search incremented");
		if (pattern != null && !pattern.isEmpty()) {
			List<User> result = userEJB.searchPattern(pattern);

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
	 * When called the service will allow for payment between accounts Try to
	 * use another REST service to accomplish this one. Specifically the GetUser
	 * service.
	 * 
	 * Find both users on the DB Debit the sender for the amount Credit the
	 * receiver for the amount Save both users to the DB.
	 * 
	 * @param sender
	 * @param receiver
	 * @param amount
	 * @return
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	@POST
	@Path("/pay")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response pay(@FormParam("sender") String sender, @FormParam("receiver") String receiver,
			@FormParam("amount") double amount) throws JsonParseException, JsonMappingException, IOException {
		ServiceAccessCounter.incrementPayCount();

		Client client = ClientBuilder.newClient();
		ObjectMapper objectMapper = new ObjectMapper();

		// get users from the DB
		if (sender != null && receiver != null && !sender.isEmpty() && !receiver.isEmpty()) {
			User sender_usr = new User();
			User receiver_usr = new User();

			// get sender
			WebTarget webTarget = client.target("http://localhost:8080/RestApp/rest/user/get/").path(sender);
			Response response = webTarget.request(MediaType.APPLICATION_JSON).get();
			String result = response.readEntity(String.class);
			sender_usr = objectMapper.readValue(result, User.class);

			// get receiver
			webTarget = client.target("http://localhost:8080/RestApp/rest/user/get/").path(receiver);
			response = webTarget.request(MediaType.APPLICATION_JSON).get();
			result = response.readEntity(String.class);
			receiver_usr = objectMapper.readValue(result, User.class);

			// update classes
			sender_usr.updateBalance(amount, false); // credit
			receiver_usr.updateBalance(amount, true); // debit

			// save the users
			userEJB.saveUser(sender_usr);
			userEJB.saveUser(receiver_usr);

			String msg = String.format("Successfully paid %s with %.2f", receiver, amount);
			return Response.ok(msg).build();
		}
		return Response.ok("Payment unsucessful").build();
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
			WebTarget webTarget = client.target("http://localhost:8080/RestApp/rest/user/get").path(username);
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
