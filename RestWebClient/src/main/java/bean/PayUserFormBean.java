package bean;

import javax.faces.bean.ManagedBean;

import javax.faces.bean.SessionScoped;

@SessionScoped
@ManagedBean(name="payUserForm")
public class PayUserFormBean {
	private String sender;
	private String receiver;
	private double amount;
	private String requestResult;
	
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amomunt) {
		this.amount = amomunt;
	}
	public String getRequestResult() {
		return requestResult;
	}
	public void setRequestResult(String requestResult) {
		this.requestResult = requestResult;
	}
}
