/**
 * Simplified version of the User class sent from the server.
 * Only purpose is to store/create User objects by using the JSON
 * that the server requires.
 * 
 */
package core;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
	@JsonProperty("id") private int id;
	@JsonProperty("username") private String username;
	@JsonProperty("password") private String password;
	@JsonProperty("balance") private double balance;
	
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

	public void setPassword(String password) {
		this.password = password;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}
}