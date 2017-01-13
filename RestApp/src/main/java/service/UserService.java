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
import javax.ws.rs.client.Invocation;
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
	public Response createUser(@FormParam("name") String username, @FormParam("password") String password) {

		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		userEJB.saveUser(user);

		Usergroup up = new Usergroup();
		up.setUsername(username);
		up.setGroupname("admin");
		upEJB.save(up);

		return Response.ok("User added successfully" + user).build();
	}

	@POST
	@Path("/delete")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteUser(@FormParam("name") String username) {
		// First delete the user from the User table
		User user = userEJB.getUser(username);
		userEJB.deleteUser(user);

		// Next delete the user from the Usergroup table
		Usergroup usergroup = upEJB.getUsergroup(username);
		upEJB.delete(usergroup);

		return Response.ok("User " + user + "was deleted").build();
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
	public Response getUser(@PathParam("user") String username) {
		User user = userEJB.getUser(username);
		return Response.ok(user).build();
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
	public Response updateUser(@FormParam("name") String username, @FormParam("old_pwd") String old_password,
			@FormParam("new_pwd") String new_password) {

		User user = userEJB.getUser(username);
		String old_pwd = user.getPassword();

		// check if provided password is same as old
		// if true set new password
		if (user.isValid() && user.generateHash(old_password).equals(old_pwd)) {
			user.setPassword(new_password);
			userEJB.saveUser(user);
			return Response.ok("User " + username + " has been updated").build();
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
	public Response searchUser(@PathParam("pattern") String pattern) {
		List<User> result = userEJB.searchPattern(pattern);

		// get a generic entity else
		// Severe: MessageBodyWriter not found for media
		// type=application/json, type=class
		// java.util.Vector, genericType=class java.util.Vector. error
		GenericEntity<List<User>> entity = new GenericEntity<List<User>>(result) {
		};
		return Response.ok(entity).build();
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
	public Response payUser(@FormParam("sender") String sender, @FormParam("receiver") String receiver,
			@FormParam("amount") double amount) throws JsonParseException, JsonMappingException, IOException {
		Client client = ClientBuilder.newClient();
		ObjectMapper objectMapper = new ObjectMapper();

		System.out.println(sender);
		System.out.println(receiver);
		System.out.println(amount);
		
		// get users from the DB
		if (sender != null && receiver != null) {
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

			return Response.ok("Successfully paid: " + receiver + " " + amount).build();
		}
		return Response.ok("Payment unsucessful").build();
	}
}
