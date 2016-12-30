/**
 * Automatically generated from tables using JPA
 */
package entity;

import java.io.Serializable;
import javax.persistence.*;


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

}