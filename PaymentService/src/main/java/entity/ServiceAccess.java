package entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the service_access database table.
 * 
 */
@Entity
@Table(name="service_access")
@NamedQuery(name="ServiceAccess.findAll", query="SELECT s FROM ServiceAccess s")
public class ServiceAccess implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ServiceAccessPK id;

	private int fail;

	private int pass;

	public ServiceAccess() {
	}

	public ServiceAccessPK getId() {
		return this.id;
	}

	public void setId(ServiceAccessPK id) {
		this.id = id;
	}

	public int getFail() {
		return this.fail;
	}

	public void setFail(int fail) {
		this.fail = fail;
	}

	public int getPass() {
		return this.pass;
	}

	public void setPass(int pass) {
		this.pass = pass;
	}

}