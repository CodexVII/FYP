/**
 * Demonstrating annotation validation
 */
package bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@ManagedBean
@ViewScoped
public class DataBean {
	@NotNull(message="Bad String")
	private String inputString;
	@Max(message="Bad Double", value = 100)
	private double inputDouble;
	@Min(value=10, message="Bad Int")
	private int inputInt;
	
	public String getInputString() {
		return inputString;
	}
	public void setInputString(String inputString) {
		this.inputString = inputString;
	}
	public double getInputDouble() {
		return inputDouble;
	}
	public void setInputDouble(double inputDouble) {
		this.inputDouble = inputDouble;
	}
	public int getInputInt() {
		return inputInt;
	}
	public void setInputInt(int inputInt) {
		this.inputInt = inputInt;
	}
}
