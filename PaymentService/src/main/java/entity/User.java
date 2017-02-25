package entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the user database table.
 * 
 */
@Entity
@NamedQuery(name="User.findAll", query="SELECT u FROM User u")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	private double balance;

	private String password;

	private String username;

	public User() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getBalance() {
		return this.balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	@Override
	public String toString() {
		return "User [id=" + id + ", balance=" + balance + ", password=" + password + ", username=" + username + "]";
	}

	/**
	 * Update the balance for the user. Increase or decrease
	 * depending on the credit parameter
	 * 
	 * @param balance
	 * @param credit
	 */
	public void updateBalance(double amount, boolean credit){
		if(credit){
			this.balance+=amount;	//credit the user
		}else{
			this.balance-=amount;	//debit the user
		}
	}

}