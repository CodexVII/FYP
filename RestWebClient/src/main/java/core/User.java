/**
 * Simplified version of the User class sent from the server.
 * Only purpose is to store/create User objects by using the JSON
 * that the server requires.
 * 
 */
package core;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The persistent class for the t_user database table.
 * 
 */
public class User {
	@JsonProperty("id") private int id;
	@JsonProperty("username") private String username;
	@JsonProperty("password") private String password;

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

}