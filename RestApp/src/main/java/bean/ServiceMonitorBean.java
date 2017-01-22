/**
 * Lesson 
 */
package bean;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import utility.ServiceAccessCounter;

@ManagedBean(name="serviceMonitor")
@ViewScoped
public class ServiceMonitorBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2609489231858111060L;
	private int searchCount;
	private int registerCount; 
	private int deleteCount;
	private int getCount;
	private int changePasswordCount;
	private int payCount;
	private int validateCount;
	
	public int getGetCount() {
		return getCount;
	}

	public void setGetCount(int getCount) {
		this.getCount = getCount;
	}

	public int getChangePasswordCount() {
		return changePasswordCount;
	}

	public void setChangePasswordCount(int updatePasswordCount) {
		this.changePasswordCount = updatePasswordCount;
	}

	public int getPayCount() {
		return payCount;
	}

	public void setPayCount(int payCount) {
		this.payCount = payCount;
	}

	public int getValidateCount() {
		return validateCount;
	}

	public void setValidateCount(int validateCount) {
		this.validateCount = validateCount;
	}

	public int getDeleteCount() {
		return deleteCount;
	}

	public void setDeleteCount(int deleteCount) {
		this.deleteCount = deleteCount;
	}

	public int getRegisterCount() {
		return registerCount;
	}

	public void setRegisterCount(int registerCount) {
		this.registerCount = registerCount;
	}

	public int getSearchCount() {
		return searchCount;
	}

	public void setSearchCount(int searchCount) {
		this.searchCount = searchCount;
	}

	/**
	 * update all values on page load
	 */
	@PostConstruct
	public void update(){
		searchCount=ServiceAccessCounter.getSearchCount();
		registerCount=ServiceAccessCounter.getRegisterCount();
		deleteCount=ServiceAccessCounter.getDeleteCount();
		changePasswordCount=ServiceAccessCounter.getChangePasswordCount();
		getCount=ServiceAccessCounter.getGetCount();
		payCount=ServiceAccessCounter.getPayCount();
		validateCount=ServiceAccessCounter.getValidateCount();
	}
}
