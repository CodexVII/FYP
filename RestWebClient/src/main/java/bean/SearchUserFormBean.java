package bean;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@SessionScoped
@ManagedBean(name="searchUserForm")
public class SearchUserFormBean implements Serializable{
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
}
