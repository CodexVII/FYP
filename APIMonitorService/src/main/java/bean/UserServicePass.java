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

@ManagedBean(name="userServicePass")
@RequestScoped
public class UserServicePass {
	private List<Counter> counters;

	@Inject
	ServiceAccessEJB saEJB;

	public List<Counter> getCounters() {
		return counters;
	}

	public void setCounters(List<Counter> counters) {
		this.counters = counters;
	}

	public UserServicePass() {
	
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
		for (int i = 0; i < sa.size(); i++) {
			// object containing the service name and operation
			currentService = sa.get(i);
			currentServicePK = currentService.getId();
			if(currentServicePK.getService().toLowerCase().equals("user")){
				counters.add(new Counter(currentServicePK.getOperation(), currentService.getPass()));	
			}
		}
	}
}
