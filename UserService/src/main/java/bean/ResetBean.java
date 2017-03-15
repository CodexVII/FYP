package bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import utility.ServiceAccessCounter;

@ManagedBean
@RequestScoped
public class ResetBean {
	/**
	 * Clears counters for this current instance
	 */
	public String clearData(){
		ServiceAccessCounter.reset();
		return "index.xhtml?faces-redirect=true";
	}
}
