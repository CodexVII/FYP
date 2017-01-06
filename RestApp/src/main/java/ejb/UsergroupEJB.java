package ejb;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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
	
	/**
	 * Find a Usergroup entity by username in the DB
	 * 
	 * @param username
	 * @return
	 */
	public Usergroup getUsergroup(String username){
		Usergroup usergroup= new Usergroup();
		try{
			Query q = em.createQuery("SELECT up FROM Usergroup up WHERE up.username LIKE :usrNm");
			usergroup = (Usergroup)q.setParameter("usrNm", username).getSingleResult();
		}catch(NoResultException e){
			System.out.println("No user with name " + username + " found the Usergroup table");
		}
		return usergroup;
	}
}
