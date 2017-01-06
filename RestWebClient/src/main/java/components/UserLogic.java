/**
 * Separating the logic for User.
 * Moves all service requests to the REST API to this class.
 * 
 */
package components;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import bean.UserBean;
import core.User;

@ManagedBean
@RequestScoped
public class UserLogic {
	@ManagedProperty(value = "#{userBean}")
	private UserBean userBean; // inject ManagedBean

	private Client client = ClientBuilder.newClient(); // REST client
	private ObjectMapper objectMapper = new ObjectMapper(); // Jackson
	private String api = "http://localhost:8080/RestApp/rest/user";

	/**
	 * Required to make the injection successful
	 * 
	 * @param userBean
	 */
	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}

	/**
	 * Call the REST service to search for the user Store result in an array of
	 * Returns users to let JSF handle what to display instead
	 * 
	 * Users
	 * 
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	public List<User> search() throws JsonParseException, JsonMappingException, IOException {
		List<User> users = new ArrayList<User>();

		// Only call the service if the @PathParam is not empty.
		if (userBean.getSearchPattern() != null) {
			WebTarget webTarget = client.target(api).path("search").path(userBean.getSearchPattern());

			Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
			Response response = invocationBuilder.get();

			// Place the JSON result in an array of User class which can be
			// accessed easily 
			//toString method not adequate!
			String result = response.readEntity(String.class);
			users = objectMapper.readValue(result, new TypeReference<List<User>>() {
			});
		}
		return users;
	}

	/**
	 * Simply call the REST service with the user. How to send info? Can pass
	 * everything through URI as usual.. security issue?
	 * 
	 * User Jersey Form and send via APPLICATION_FORM_URL_ENCODED
	 * 
	 * Could send as JSONObject instead
	 */
	public void updatePassword() {
		WebTarget webTarget = client.target(api).path("update").path("password");

		// build form data
		Form form = new Form();
		form.param("name", userBean.getName());
		form.param("old_pwd", userBean.getPassword());
		form.param("new_pwd", userBean.getRequestedPassword());

		// send to REST service
		// read response
		Response response = webTarget.request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), Response.class);
		String result = response.readEntity(String.class);
		
		//add the result to the bean.
		userBean.setPasswordChangeResult(result);
	}
	
	/**
	 * Adds user to the database
	 * 
	 * 
	 * Create a form using data taken from UserBean
	 * Send to REST service
	 * 
	 * Store result of the action
	 */
	public void addUser(){
		WebTarget webTarget = client.target(api).path("add");
		
		//build form data
		Form form = new Form();
		form.param("name", userBean.getName());
		form.param("password", userBean.getPassword());
		
		//send form to REST service
		Response response = webTarget.request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), Response.class);
		String result = response.readEntity(String.class);
		
		userBean.setCreateUserResult(result);
	}
	
	public void deleteUser(){
		WebTarget webTarget = client.target(api).path("delete");
		
		Form form = new Form();
		form.param("name", userBean.getName());
		
		Response response = webTarget.request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), Response.class);
		String result = response.readEntity(String.class);
		
		userBean.setDeleteUserResult(result);
	}
	
	/**
	 * Get a single user
	 * Much like the search pattern one, a test must be made to ensure
	 * that the user being searched is not null
	 * 
	 * ajax has a bad habit of running without even clicking the submit button.
	 * (reason is async maybe?)
	 * 
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public User getUser() throws JsonParseException, JsonMappingException, IOException{
		User user = new User();
		
		if(userBean.getName()!=null){
			WebTarget webTarget = client.target(api).path("get").path(userBean.getName());
			
			Response response = webTarget.request(MediaType.APPLICATION_JSON)
					.get();
			
			String result = response.readEntity(String.class);
			user = objectMapper.readValue(result, User.class);
		}
		return user;
	}
}
