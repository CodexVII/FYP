/**
 * Attributes/Methods called inside index.xhtml
 * 
 */
package bean;

import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;

import ejb.UserEJB;
import ejb.UsergroupEJB;
import entity.User;
import entity.Usergroup;

@ApplicationScoped
@ManagedBean
public class UserBean {
	private String name;
	private String password;

	//Inject user EJB into the bean. Requires a bean.xml inside lib folder
	//Glassfish needs this to know that there are beans. Checks for the annotation.
	//Can also define beans in it but old method. Annotations more readable
	@Inject
	UserEJB userEJB;	
	@Inject
	UsergroupEJB upEJB;
	
	public List<User> getAllUsers(){
		return userEJB.getAll();
	}
	
	public void add(){
		//fancy logic for adding 
		//call REST service at URL
		//pass params if required.
		
		User user = new User();
		user.setUsername(name);
		user.setPassword(password);
		
		Usergroup up = new Usergroup();
		up.setUsername(name);
		up.setGroupname("admin");
		
		userEJB.saveUser(user);
		upEJB.save(up);
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}