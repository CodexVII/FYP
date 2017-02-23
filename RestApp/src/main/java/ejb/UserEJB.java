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

@LocalBean // makes into a bean
@Stateless // attempting to make it work for injection in REST service
public class UserEJB {
	@PersistenceContext
	EntityManager em;

	public void saveUser(User user) {
		em.merge(user); // merge will save or update as necessary
	}

	public void deleteUser(User user) {
		em.remove(user);
	}

	/**
	 * Finds user in DB and returns it if it exists getSingleResult throwing out
	 * errors
	 * 
	 * @param username
	 * @return
	 */
	public User getUser(String username) throws Exception {
		User user = new User();

		Query q = em.createQuery("SELECT user FROM User user WHERE user.username LIKE :usrNm");
		user = (User) q.setParameter("usrNm", username).getSingleResult();

		return user;
	}

	/**
	 * Returns a list of users from users table containing the pattern provided
	 * 
	 * @param pattern
	 * @return
	 */
	public List<User> patternSearch(String pattern) {
		Query q = em.createQuery("SELECT user FROM User user WHERE user.username LIKE :ptrn");
		q.setParameter("ptrn", "%" + pattern + "%"); // result simply needs to
														// contain pattern.
														// outisde are wildcards
														// with %.
		List<User> users = (List<User>) q.getResultList();

		return users;
	}
}
