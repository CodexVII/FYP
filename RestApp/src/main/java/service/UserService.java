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
	 * Add user to the database.
	 * Automatically adds the users to the admin group
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
	public Response deleteUser(@FormParam("name") String username){
		User user = userEJB.getUser(username);
		userEJB.deleteUser(user);
		return Response.ok("User " + user + "was deleted").build();
	}
}
