package ejb;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import entity.Transaction;

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
//		Query q = em.createQuery("SELECT t FROM Transaction WHERE t.sender LIKE :send OR t.receiver LIKE :receive");
//		q.setParameter("send", username);
//		q.setParameter("receive", username);
		Query q = em.createQuery("SELECT t FROM Transaction t WHERE (t.sender LIKE :user OR t.receiver LIKE :user)").setParameter("user", username);
		transactions = (List<Transaction>) q.getResultList();  
				
		return transactions;
	}
}
	