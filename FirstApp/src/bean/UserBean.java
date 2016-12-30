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
import entity.User;

@ApplicationScoped
@ManagedBean
public class UserBean {
	private String name;
	//Inject user EJB into the bean. Requires a bean.xml inside lib folder
	//Glassfish needs this to know that there are beans. Checks for the annotation.
	//Can also define beans in it but old method. Annotations more readable
	@Inject
	UserEJB userEJB;	
	
	public List<User> getAllUsers(){
		return userEJB.getAll();
	}
	
	public void add(){
		User user = new User();
		user.setUsername(name);
		userEJB.saveUser(user);
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}

}
