package bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name="benchmarkForm")
@SessionScoped
public class BenchmarkFormBean {
	private String service ="1";	//service taken from JSF combo box
	
	public void setService(String service){
		this.service = service;
	}
	public String getService(){
		return service;
	}
}
