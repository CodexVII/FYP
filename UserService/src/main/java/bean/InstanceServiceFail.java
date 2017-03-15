package bean;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import utility.Counter;
import utility.ServiceAccessCounter;

@ManagedBean
@RequestScoped
public class InstanceServiceFail {
	private List<Counter> counters;

	public List<Counter> getCounters() {
		return counters;
	}

	public void setCounters(List<Counter> counters) {
		this.counters = counters;
	}

	public InstanceServiceFail() {
		reload();
	}

	private void reload() {
		counters = new ArrayList<Counter>();
		
		// use temp list to ignore empty counters
		List<Counter> tmpCounters = ServiceAccessCounter.getAllFail();
		Counter currCount;
		for(int i=0; i<tmpCounters.size(); i++){
			currCount = tmpCounters.get(i);
			// only add the counter to the graph if it has an amount
			if(currCount.getAmount() > 0){
				counters.add(currCount);
			}
		}
	}
}
