/**
 * Attributes/Methods called inside index.xhtml
 * 
 */
package bean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
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

import core.User;

@SessionScoped
@ManagedBean(name="userBean")	//allows for injectiono
public class UserBean {
	private String name;
	private String password;
	private String searchPattern;	//could be moved to another class and injected
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	
	public String getSearchPattern() {
		return searchPattern;
	}

	public void setSearchPattern(String searchPattern) {
		this.searchPattern = searchPattern;
	}	
}
