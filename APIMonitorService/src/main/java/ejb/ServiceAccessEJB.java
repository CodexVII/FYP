package ejb;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import entity.ServiceAccess;
import entity.ServiceAccessPK;

@LocalBean
@Stateless
public class ServiceAccessEJB {
	@PersistenceContext
	EntityManager em;

	public void saveServiceAccess(ServiceAccess sa) {
		em.merge(sa);
	}

	/**
	 * Return all service access members
	 * 
	 * @return
	 */
	public List<ServiceAccess> getAll() {
		List<ServiceAccess> sa = new ArrayList<ServiceAccess>();
		Query q = em.createQuery("SELECT s FROM ServiceAccess s");
		sa = (List<ServiceAccess>) q.getResultList();
		return sa;
	}

	/**
	 * ALSO AUTO GENERATES !!!
	 * 
	 * @param service
	 * @param operation
	 * @return
	 */
	public ServiceAccess getServiceAccess(String service, String operation) {
		// create a PK object first which is used by the EntityManager to
		// find the entity
		ServiceAccessPK pk = new ServiceAccessPK(service, operation);
		ServiceAccess sa = em.find(ServiceAccess.class, pk);

		// if it's null we assume that it hasn't been created yet but should
		// be a valid row, so we create one.
		if(sa == null){
			sa = new ServiceAccess(pk);
			
			// commit it to the DB before returning it.
			em.merge(sa);
		}
		return sa;
	}

	public void incrementFail(String service, String operation) {
		// get service row
		ServiceAccessPK pk = new ServiceAccessPK(service, operation);
		ServiceAccess sa = em.find(ServiceAccess.class, pk);

		// increment failure
		sa.setFail(sa.getFail() + 1);

		// update the row with the new fail count
		em.merge(sa);
	}

	/**
	 * Called when service was requested however there was an error in
	 * processing the request.
	 * 
	 * Increments the failure count for this specific service on the DB
	 * 
	 * @param service
	 * @param type
	 * @param error
	 */
	public void incrementPass(String service, String type) {
		// get service row
		ServiceAccessPK pk = new ServiceAccessPK(service, type);
		ServiceAccess sa = em.find(ServiceAccess.class, pk);

		// increment failure
		sa.setPass(sa.getPass() + 1);

		// update the row with the new count
		em.merge(sa);
	}
}
