package bean;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@SessionScoped
@ManagedBean(name="getUserForm")
public class GetUserFormBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1023141315318101475L;
	private String username;
	private String requestResult;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getRequestResult() {
		return requestResult;
	}
	public void setRequestResult(String requestResult) {
		this.requestResult = requestResult;
	}
	
	public void feedback(){
		FacesContext context = FacesContext.getCurrentInstance();
		
		//decide on which message to display to the user based
		//on service response
		if(requestResult != null && requestResult.contains("success")){
			FacesMessage success = new FacesMessage("User found");
			success.setSeverity(FacesMessage.SEVERITY_INFO);
			context.addMessage(null, success);
		}else{
			FacesMessage fail = new FacesMessage("Requested user not found");
			fail.setSeverity(FacesMessage.SEVERITY_ERROR);
			context.addMessage(null, fail);
		}
	}
}
