package bean;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import utility.Counter;
import utility.ServiceAccessCounter;

@ManagedBean
@RequestScoped
public class PieChartBean {
	private List<Counter> counters;

	public List<Counter> getCounters() {
		return counters;
	}

	public void setCounters(List<Counter> counters) {
		this.counters = counters;
	}

	public PieChartBean() {
		reload();
	}

	private void reload() {
		counters = new ArrayList<Counter>();

		// generate counter objects to add to the list
		Counter searchCount = new Counter("search", ServiceAccessCounter.getSearchCount());
		Counter registerCount = new Counter("register", ServiceAccessCounter.getRegisterCount());
		Counter deleteCount = new Counter("delete", ServiceAccessCounter.getDeleteCount());
		Counter getCount = new Counter("get", ServiceAccessCounter.getGetCount());
		Counter changePasswordCount = new Counter("change password", ServiceAccessCounter.getChangePasswordCount());
		Counter payCount = new Counter("pay", ServiceAccessCounter.getPayCount());
		Counter validateCount = new Counter("validate", ServiceAccessCounter.getValidateCount());

		if (searchCount.getAmount() > 0) {
			counters.add(searchCount);
		}

		if (registerCount.getAmount() > 0) {
			counters.add(registerCount);
		}

		if (deleteCount.getAmount() > 0) {
			counters.add(deleteCount);
		}
		if (getCount.getAmount() > 0) {
			counters.add(getCount);
		}
		if (changePasswordCount.getAmount() > 0) {
			counters.add(changePasswordCount);
		}
		if (payCount.getAmount() > 0) {
			counters.add(payCount);
		}
		if (validateCount.getAmount() > 0) {
			counters.add(validateCount);
		}

		// add the counters to the data list. This will be the data to be
		// plotted
		// counters.add(new Counter("search",
		// ServiceAccessCounter.getSearchCount()));
		// counters.add(new Counter("register",
		// ServiceAccessCounter.getRegisterCount()));
		// counters.add(new Counter("delete",
		// ServiceAccessCounter.getDeleteCount()));
		// counters.add(new Counter("get", ServiceAccessCounter.getGetCount()));
		// counters.add(new Counter("change password",
		// ServiceAccessCounter.getChangePasswordCount()));
		// counters.add(new Counter("pay", ServiceAccessCounter.getPayCount()));
		// counters.add(new Counter("validate",
		// ServiceAccessCounter.getValidateCount()));

		for (int i = 0; i < counters.size(); i++) {
			System.out.println(counters.get(i));
		}
	}
}
