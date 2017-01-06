/**
 * Attributes/Methods called inside index.xhtml
 * 
 */
package bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@SessionScoped
@ManagedBean(name="userBean")	//allows for injectiono
public class UserBean {
	private String name;
	private String password;
	private String searchPattern;	//could be moved to another class and injected
	private String requestedPassword;
	private String passwordChangeResult;
	
	public String getPasswordChangeResult() {
		return passwordChangeResult;
	}

	public void setPasswordChangeResult(String passwordChangeResult) {
		this.passwordChangeResult = passwordChangeResult;
	}

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

	public String getRequestedPassword() {
		return requestedPassword;
	}

	public void setRequestedPassword(String requestedPassword) {
		this.requestedPassword = requestedPassword;
	}	
	
	
}
