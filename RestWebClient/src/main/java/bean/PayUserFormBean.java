package bean;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@SessionScoped
@ManagedBean(name="payUserForm")
public class PayUserFormBean {
	private String sender;
	private String receiver;
	private double amount = 0.00;
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
	public void feedback(){
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage msg = new FacesMessage();
		
		if(requestResult != null && requestResult.toLowerCase().contains("success")){
			String detail = String.format("Successfully paid %s with %.2f", receiver, amount);
			msg.setDetail(detail);
			msg.setSeverity(FacesMessage.SEVERITY_INFO);
			context.addMessage(null, msg);
		}else{
			msg.setDetail("Error processing payment");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			context.addMessage(null, msg);
		}
	}
}
