/**
 * Handles db connection to user table
 */
package ejb;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
	
	/**
	 * Finds user in DB and returns it if it exists
	 * getSingleResult throwing out errors
	 * @param username
	 * @return
	 */
	public User getUser(String username){
		User user = new User();
		try{
			Query q = em.createQuery("SELECT user FROM User user WHERE user.username LIKE :usrNm");
			user = (User)q.setParameter("usrNm", username).getSingleResult();
		}catch(NoResultException e){
			System.out.println("No user with name " + username + " found");
		}
		return user;
	}
	
	/**
	 * Returns a list of users from users table containing the pattern provided
	 * 
	 * @param pattern
	 * @return
	 */
	public List<User> searchPattern(String pattern){
		Query q = em.createQuery("SELECT user FROM User user WHERE user.username LIKE :ptrn");
		q.setParameter("ptrn", "%" +pattern + "%");	//result simply needs to contain pattern.
													//outisde are wildcards with %.
		List<User> users = (List<User>)q.getResultList();
		
		return users;
	}
}
