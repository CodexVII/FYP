package bean;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@SessionScoped
@ManagedBean(name="updateUserPasswordForm")
public class UpdateUserPasswordFormBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6363934487258518831L;
	private String username;
	private String currentPassword;
	private String newPassword;
	private String requestResult;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getCurrentPassword() {
		return currentPassword;
	}
	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public String getRequestResult() {
		return requestResult;
	}
	public void setRequestResult(String requestResult) {
		this.requestResult = requestResult;
	}
	/**
	 * Provides feedback to the user in the form of a FacesMessage
	 * Called within the UserLogic class.
	 */
	public void feedback(){
		FacesContext context = FacesContext.getCurrentInstance();
		
		if(requestResult != null && requestResult.contains("success")){
			FacesMessage success = new FacesMessage("Password updated successfully");
			success.setSeverity(FacesMessage.SEVERITY_INFO);
			context.addMessage(null, success);
		}else{
			FacesMessage fail = new FacesMessage("Failed to update password for specified user");
			fail.setSeverity(FacesMessage.SEVERITY_ERROR);
			context.addMessage(null, fail);
		}
		
	}
}
