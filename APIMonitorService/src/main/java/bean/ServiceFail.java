package bean;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;

import ejb.ServiceAccessEJB;
import entity.ServiceAccess;
import entity.ServiceAccessPK;
import utility.Counter;

/**
 * Displays all stored service counters
 * 
 * @author keita
 *
 */
@ManagedBean(name = "serviceFail")
@RequestScoped
public class ServiceFail {
	private List<Counter> counters;

	@Inject
	ServiceAccessEJB saEJB;

	public List<Counter> getCounters() {
		return counters;
	}

	public void setCounters(List<Counter> counters) {
		this.counters = counters;
	}

	public ServiceFail() {

	}

	@PostConstruct
	private void reload() {
		counters = new ArrayList<Counter>();

		// get counter values from db... how?
		List<ServiceAccess> sa = saEJB.getAll();
		// have the name and pass/fail count of each object

		// loop through the result...
		// get the operation and count...
		// add to the counter list
		ServiceAccess currentService;
		ServiceAccessPK currentServicePK;
		int userPass = 0;
		int paymentPass = 0;

		for (int i = 0; i < sa.size(); i++) {
			// object containing the service name and operation
			currentService = sa.get(i);
			currentServicePK = currentService.getId();

			// get all failed counter values
			switch (currentServicePK.getService()) {
			case "user":
				userPass += currentService.getFail();
				break;
			case "payment":
				paymentPass += currentService.getFail();
			}
		}
		
		// all failed counters found - throw into list for graphing
		if(userPass>0){
			counters.add(new Counter("User", userPass));
		}
		if(paymentPass>0){
			counters.add(new Counter("Payment", paymentPass));	
		}
	}
}
