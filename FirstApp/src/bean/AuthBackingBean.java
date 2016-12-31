/**
 * Bean used to logout class by logging out from current session.
 */
package bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@RequestScoped
@ManagedBean
public class AuthBackingBean {
	public String logout(){
		String result = "/index?faces-redirect=true";
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();
		
		try{
			request.logout();
		}catch(ServletException e){
			result = "/loginError?faces-redirect=true";
		}
		return result;
	}
}
