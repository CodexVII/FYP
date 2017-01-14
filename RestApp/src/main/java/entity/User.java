/**
 * Automatically generated from tables using JPA
 */
package entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the t_user database table.
 * 
 */
@Entity
@Table(name="t_user")
@NamedQuery(name="User.findAll", query="SELECT u FROM User u")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	private String username;
	private String password;
	private double balance;
	
	public User() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String user) {
		this.username = user;
	}

	public String getPassword() {
		return password;
	}

	
	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
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
	
	/**
	 * Use SHA-256 hash before saving password
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * Generate a SHA256 password
	 * @return
	 */
	public String generateHash(String password){
		try {
			MessageDigest md;
			md = MessageDigest.getInstance("SHA-256");
			md.update(password.getBytes("UTF-8"));
			
			byte[] digest = md.digest();
			String hash = String.format("%064x", new java.math.BigInteger(1, digest));
			return hash;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Remove side effect of the JSON ObjectMapper from hashing
	 * current password again.
	 * 
	 * @param password
	 */
	public void hashPassword(String password){
		setPassword(generateHash(password));
	}
	public boolean isValid(){
		return username!=null && password!=null;
	}
}