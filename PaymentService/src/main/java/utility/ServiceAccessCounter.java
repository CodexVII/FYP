/**
 * Counts the amount of times the service has been called in the API
 */
package utility;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class ServiceAccessCounter {
	private static int payPass;
	private static int payFail;
	private static int transactionHistoryPass;
	private static int transactionHistoryFail;

	private static Client client = ClientBuilder.newClient(); // REST client

	
	public static int getPayPass() {
		return payPass;
	}

	public static void setPayPass(int payPass) {
		ServiceAccessCounter.payPass = payPass;
	}

	public static int getPayFail() {
		return payFail;
	}

	public static void setPayFail(int payFail) {
		ServiceAccessCounter.payFail = payFail;
	}

	public static int getGetHistoryPass() {
		return transactionHistoryPass;
	}

	public static void setGetHistoryPass(int transactionHistoryPass) {
		ServiceAccessCounter.transactionHistoryPass = transactionHistoryPass;
	}

	public static int getGetHistoryFail() {
		return transactionHistoryFail;
	}

	public static void setGetHistoryFail(int transactionHistoryFail) {
		ServiceAccessCounter.transactionHistoryFail = transactionHistoryFail;
	}

	public static Client getClient() {
		return client;
	}

	public static void setClient(Client client) {
		ServiceAccessCounter.client = client;
	}

	/**
	 * Log the service count to this instance and then into the DB by requesting
	 * the Monitoring service.
	 * 
	 * @param operation
	 */
	public static void servicePass(String operation) {
		// log for this current instance
		switch (operation) {
		case "pay":
			payPass++;
			break;
		case "transaction history":
			transactionHistoryPass++;
			break;
		}

		// send result to DB
		persistServicePass(operation);
	}

	/**
	 * Log the service count to this instance and then into the DB by requesting
	 * the Monitoring service.
	 * 
	 * @param operation
	 */
	public static void serviceFail(String operation) {
		// log for this current instance
		switch (operation) {
		case "pay":
			payFail++;
			break;
		case "transaction history":
			transactionHistoryFail++;
			break;
		}

		// send result to DB
		persistServiceFail(operation);
	}

	/**
	 * Helper method to return List of counters
	 * Counter names are the tick labels for graping
	 * @param operation
	 */
	public static List<Counter> getAllPass() {
		List<Counter> counters = new ArrayList<Counter>();
		// all passed
		counters.add(new Counter("pay", payPass));
		counters.add(new Counter("transaction history", transactionHistoryPass));
		return counters;
	}

	public static List<Counter> getAllFail() {
		List<Counter> counters = new ArrayList<Counter>();
		// all failed
		counters.add(new Counter("pay", payFail));
		counters.add(new Counter("transaction history", transactionHistoryFail));
		return counters;
	}

	private static void persistServicePass(String operation) {
		WebTarget webTarget = client.target(Constants.MONITOR_API).path("log").path("pass");

		Form form = new Form();
		form.param("service", "payment"); // unique for each service
		form.param("operation", operation);

		webTarget.request(MediaType.APPLICATION_JSON).post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED),
				Response.class);
	}

	private static void persistServiceFail(String operation) {
		WebTarget webTarget = client.target(Constants.MONITOR_API).path("log").path("fail");

		Form form = new Form();
		form.param("service", "payment"); // unique for each service
		form.param("operation", operation);

		webTarget.request(MediaType.APPLICATION_JSON).post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED),
				Response.class);
	}

	public static void reset() {
		payPass=0;
		payFail=0;
		transactionHistoryPass=0;
		transactionHistoryFail=0;
	}
}
