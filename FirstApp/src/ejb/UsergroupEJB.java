package ejb;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import entity.Usergroup;

@LocalBean
@Stateless
public class UsergroupEJB {
	@PersistenceContext
	EntityManager em;
	
	public void save(Usergroup up){
		em.merge(up);
	}
	
	public void delete(Usergroup up){
		em.remove(up);
	}
}
