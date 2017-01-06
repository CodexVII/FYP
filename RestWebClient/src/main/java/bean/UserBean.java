/**
 * Attributes/Methods called inside index.xhtml
 * These attributes are used to call REST services and render the results
 * from those services to the page it's being used in.
 */
package bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@RequestScoped
@ManagedBean(name="userBean")	//allows for injectiono
public class UserBean {
	private String name;
	private String password;
	private String searchPattern;	//could be moved to another class and injected
	private String requestedPassword;
	private String passwordChangeResult;
	private String createUserResult;
	private String deleteUserResult;
	
	public String getDeleteUserResult() {
		return deleteUserResult;
	}

	public void setDeleteUserResult(String deleteUserResult) {
		this.deleteUserResult = deleteUserResult;
	}

	public String getCreateUserResult() {
		return createUserResult;
	}

	public void setCreateUserResult(String createUserResult) {
		this.createUserResult = createUserResult;
	}

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
