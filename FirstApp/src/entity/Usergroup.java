package entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the t_usergroup database table.
 * 
 */
@Entity
@Table(name="t_usergroup")
@NamedQuery(name="Usergroup.findAll", query="SELECT u FROM Usergroup u")
public class Usergroup implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	private String groupname;

	private String username;

	public Usergroup() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getGroupname() {
		return this.groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}