package bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;

import ejb.ServiceAccessEJB;

@ManagedBean
@RequestScoped
public class ResetBean {
	@Inject
	ServiceAccessEJB saEJB;
	/**
	 * Clears counters for this current instance
	 */
	public String clearData() {
		saEJB.truncate();
		return "index.xhtml?faces-	redirect=true";
	}
}
