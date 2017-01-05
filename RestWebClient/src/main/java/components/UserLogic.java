package components;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import bean.UserBean;
import core.User;

@ManagedBean
@SessionScoped
public class UserLogic {
	@ManagedProperty(value="#{userBean}")
	private UserBean userBean;		//inject ManagedBean
	
	private Client client = ClientBuilder.newClient(); // REST client
	private ObjectMapper objectMapper = new ObjectMapper();	//Jackson
	/**
	 * Required to make the injection successful
	 * 
	 * @param userBean
	 */
	public void setUserBean(UserBean userBean){
		this.userBean = userBean;
	}
	/**
	 * Call the REST service to search for the user Store result in an array of
	 * Returns users to let JSF handle what to display instead
	 * 
	 * 
	 * Users
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public List<User> search() throws JsonParseException, JsonMappingException, IOException {
		List<User> users = new ArrayList<User>();
		
		//Only call the service if the @PathParam is not empty.
		if(userBean.getSearchPattern() != ""){
			WebTarget webTarget = client.target("http://localhost:8080/RestApp/rest/user/search/"+userBean.getSearchPattern());

			Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
			Response response = invocationBuilder.get();
			
			//Place the JSON result in an array of User class which can be accessed
			//easily
			//toString method not adequate!
			String result = response.readEntity(String.class);
			users = objectMapper.readValue(result,  new TypeReference<List<User>>(){});
		}
		return users;
	}
}
