package service;

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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
	public Response createUser(@FormParam("name") String username, @FormParam("password") String password) {
		System.out.println("Called!");
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
		User user = userEJB.getUser(username);
		userEJB.deleteUser(user);
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
	@Path("/get")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUser(@QueryParam("name") String username) {
		User user = userEJB.getUser(username);
		return Response.ok(user).build();
	}

	/**
	 * Find user Do a check on the password If valid, update else return error
	 * 
	 * @param username
	 * @return
	 */
	@POST
	@Path("/update/password")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateUser(@FormParam("name") String username, @FormParam("old_pwd") String old_password,
			@FormParam("new_pwd") String new_password) {
		User user = userEJB.getUser(username);
		String old_pwd = user.getPassword();

		// check if provided password is same as old
		// if true set new password
		if (user.generateHash(old_password).equals(old_pwd)) {
			user.setPassword(new_password);
			userEJB.saveUser(user);
			return Response.ok(user).build();
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
}
