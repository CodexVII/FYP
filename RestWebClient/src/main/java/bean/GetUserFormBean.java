package bean;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

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
}
