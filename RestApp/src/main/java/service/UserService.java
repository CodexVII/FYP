package service;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ejb.UserEJB;
import entity.User;

@Path("/user")
@Stateless
public class UserService {
	@Inject
	UserEJB userEJB;

	/**
	 * Add user to the database. BOOM.
	 * 
	 * @param user
	 * @return
	 */
	@POST
	@Path("/add")
	@Produces(MediaType.APPLICATION_JSON)
	public Response createUser(@FormParam("name") String username, @FormParam("password") String password) {
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		userEJB.saveUser(user);
		return Response.ok("User added successfully" + user).build();
	}
}
