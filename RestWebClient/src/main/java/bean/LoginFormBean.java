/**
 * Used to login
 * 
 * Class should also contain session-wide details about the user
 * such as username, balance etc.
 */
package bean;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@SessionScoped
@ManagedBean(name="loginForm")
public class LoginFormBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6820068188200808860L;
	private String username;
	private String password;
	private double balance;
	private String requestResult;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public String getRequestResult() {
		return requestResult;
	}
	public void setRequestResult(String requestResult) {
		this.requestResult = requestResult;
	}
	
	/**
	 * Redirect on success else provide an error message
	 * 
	 * @return 	true: the request was a success
	 * 			false: the request failed
	 */
	public boolean feedback(){
		//create message for the user
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage msg = new FacesMessage();
		//check result form server
		if(requestResult != null && requestResult.contains("success")){
			return true;
		}else{
			msg.setDetail("Failed to log user in");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			context.addMessage(null, msg);
		}
		return false;
	}
}
