package bean;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@SessionScoped
@ManagedBean(name = "searchUserForm")
public class SearchUserFormBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6364032952752705941L;
	private String searchPattern;
	private String requestResult;

	public String getSearchPattern() {
		return searchPattern;
	}

	public void setSearchPattern(String searchPattern) {
		this.searchPattern = searchPattern;
	}

	public String getRequestResult() {
		return requestResult;
	}

	public void setRequestResult(String requestResult) {
		this.requestResult = requestResult;
	}

	public void feedback() {
		FacesContext context = FacesContext.getCurrentInstance();

		// decide on which message to display to the user based
		// on service response
		if (requestResult != null && requestResult.contains("success")) {
			FacesMessage success = new FacesMessage("User(s) found");
			success.setSeverity(FacesMessage.SEVERITY_INFO);
			context.addMessage(null, success);
		} else {
			FacesMessage fail = new FacesMessage("Search returned empty");
			fail.setSeverity(FacesMessage.SEVERITY_ERROR);
			context.addMessage(null, fail);
		}
	}
}
