package ejb;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transaction;

/**
 * Saves transactions
 * 
 * @author keita
 *
 */
@LocalBean
@Stateless
public class TransactionEJB {
	@PersistenceContext
	EntityManager em;
	
	public void saveTransaction(Transaction transaction){
		em.merge(transaction);
	}
	
	/**
	 * Get a list of transactions from the table using the username.
	 * Look for any rows where the username exists
	 * 	
	 * @param username
	 * @return
	 */
	public List<Transaction> getTransactions(String username){
		List<Transaction> transactions = new ArrayList<Transaction>();
		Query q = em.createQuery("SELECT s FROM Transaction WHERE s.sender LIKE :usrNm OR s.receiver LIKE :usrNm");
		transactions = (List<Transaction>) q.setParameter("usrNm", username).getResultList();
		return transactions;
	}
}
	