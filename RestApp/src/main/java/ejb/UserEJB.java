/**
 * Handles db connection to user table
 */
package ejb;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import entity.User;

@LocalBean	//makes into a bean
@Stateless	//attempting to make it work for injection in REST service
public class UserEJB {
	@PersistenceContext
	EntityManager em;
	
	public void saveUser(User user){
		em.merge(user); //merge will save or update as necessary
	}
	
	public void deleteUser(User user){
		em.remove(user);
	}
	
	// get back all users from table
	public List<User> getAll(){
		Query q = em.createQuery("SELECT user FROM User user");
		return(List<User>)q.getResultList();
	}
	
	public User getUser(String username){
		User user = null;
		Query q = em.createQuery("SELECT user FROM User user WHERE user.username LIKE :usrNm");
		user = (User)q.setParameter("usrNm", username).getSingleResult();
		return user;
	}
}
