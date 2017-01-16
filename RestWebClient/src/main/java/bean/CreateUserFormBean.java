package bean;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@SessionScoped
@ManagedBean(name="createUserForm")
public class CreateUserFormBean {
	private String username;
	private String password;
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
	public String getRequestResult() {
		return requestResult;
	}
	public void setRequestResult(String requestResult) {
		this.requestResult = requestResult;
	}
	
	public void feedback(){
		//create a message for the user
		FacesContext context = FacesContext.getCurrentInstance();
		
		//check result from server
		if(requestResult != null && requestResult.contains("success")){
			FacesMessage success = new  FacesMessage("User added successfully");
			success.setSeverity(FacesMessage.SEVERITY_INFO);
			context.addMessage(null, success);
		}else{
			FacesMessage fail = new FacesMessage("Error registering user");
			fail.setSeverity(FacesMessage.SEVERITY_ERROR);
			context.addMessage(null, fail);
		}
	}
	
}
