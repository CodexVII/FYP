/**
 * Handles db connection to user table
 */
package ejb;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import entity.User;

@LocalBean // makes into a bean
@Stateless // attempting to make it work for injection in REST service
public class UserEJB {
	@PersistenceContext
	EntityManager em;

	public void saveUser(User user) {
		em.merge(user); // merge will save or update as necessary
	}
	
}
