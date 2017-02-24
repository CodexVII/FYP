package entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the service_access database table.
 * 
 */
@Embeddable
public class ServiceAccessPK implements Serializable {
	// default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	private String service;

	private String operation;

	public ServiceAccessPK() {
	}

	public ServiceAccessPK(String service, String operation) {
		super();
		this.service = service;
		this.operation = operation;
	}

	public String getService() {
		return this.service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getOperation() {
		return this.operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof ServiceAccessPK)) {
			return false;
		}
		ServiceAccessPK castOther = (ServiceAccessPK) other;
		return this.service.equals(castOther.service) && this.operation.equals(castOther.operation);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.service.hashCode();
		hash = hash * prime + this.operation.hashCode();

		return hash;
	}
}