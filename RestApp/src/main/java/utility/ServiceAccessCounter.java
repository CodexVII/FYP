/**
 * Counts the amount of times the service has been called in the API
 */
package utility;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.List;

public class ServiceAccessCounter {
	private static int searchPass;
	private static int searchFail;
	private static int registerPass;
	private static int registerFail;
	private static int deletePass;
	private static int deleteFail;
	private static int getPass;
	private static int getFail;
	private static int updatePasswordPass;
	private static int updatePasswordFail;
	private static int payCount;
	private static int validatePass;
	private static int validateFail;

	private static Client client = ClientBuilder.newClient(); // REST client

	public static int getSearchPass() {
		return searchPass;
	}

	public static void setSearchPass(int searchPass) {
		ServiceAccessCounter.searchPass = searchPass;
	}

	public static int getSearchFail() {
		return searchFail;
	}

	public static void setSearchFail(int searchFail) {
		ServiceAccessCounter.searchFail = searchFail;
	}

	public static int getRegisterPass() {
		return registerPass;
	}

	public static void setRegisterPass(int registerPass) {
		ServiceAccessCounter.registerPass = registerPass;
	}

	public static int getRegisterFail() {
		return registerFail;
	}

	public static void setRegisterFail(int registerFail) {
		ServiceAccessCounter.registerFail = registerFail;
	}

	public static int getDeletePass() {
		return deletePass;
	}

	public static void setDeletePass(int deletePass) {
		ServiceAccessCounter.deletePass = deletePass;
	}

	public static int getDeleteFail() {
		return deleteFail;
	}

	public static void setDeleteFail(int deleteFail) {
		ServiceAccessCounter.deleteFail = deleteFail;
	}

	public static int getGetPass() {
		return getPass;
	}

	public static void setGetPass(int getPass) {
		ServiceAccessCounter.getPass = getPass;
	}

	public static int getGetFail() {
		return getFail;
	}

	public static void setGetFail(int getFail) {
		ServiceAccessCounter.getFail = getFail;
	}

	public static int getChangePasswordPass() {
		return updatePasswordPass;
	}

	public static void setChangePasswordPass(int changePasswordPass) {
		ServiceAccessCounter.updatePasswordPass = changePasswordPass;
	}

	public static int getChagePasswordFail() {
		return updatePasswordFail;
	}

	public static void setChagePasswordFail(int chagePasswordFail) {
		ServiceAccessCounter.updatePasswordFail = chagePasswordFail;
	}

	public static int getPayCount() {
		return payCount;
	}

	public static void setPayCount(int payCount) {
		ServiceAccessCounter.payCount = payCount;
	}

	public static int getValidatePass() {
		return validatePass;
	}

	public static void setValidatePass(int validatePass) {
		ServiceAccessCounter.validatePass = validatePass;
	}

	public static int getValidateFail() {
		return validateFail;
	}

	public static void setValidateFail(int validateFail) {
		ServiceAccessCounter.validateFail = validateFail;
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
		case "search":
			searchPass++;
			break;
		case "get":
			getPass++;
			break;
		case "register":
			registerPass++;
			break;
		case "delete":
			deletePass++;
			break;
		case "validate":
			validatePass++;
			break;
		case "update password":
			updatePasswordPass++;
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
		case "search":
			searchFail++;
			break;
		case "get":
			getFail++;
			break;
		case "register":
			registerFail++;
			break;
		case "delete":
			deleteFail++;
			break;
		case "validate":
			validateFail++;
			break;
		case "update password":
			updatePasswordFail++;
			break;

		}

		// send result to DB
		persistServiceFail(operation);
	}

	/**
	 * Helper method to return List of counters
	 * 
	 * @param operation
	 */
	public static List<Counter> getAllPass() {
		List<Counter> counters = new ArrayList<Counter>();
		// all passed
		counters.add(new Counter("search", searchPass));
		counters.add(new Counter("get", getPass));
		counters.add(new Counter("register", registerPass));
		counters.add(new Counter("delete", deletePass));
		counters.add(new Counter("validate", validatePass));
		counters.add(new Counter("update password", updatePasswordPass));
		return counters;
	}

	public static List<Counter> getAllFail() {
		List<Counter> counters = new ArrayList<Counter>();
		// all failed
		counters.add(new Counter("search", searchFail));
		counters.add(new Counter("get", getFail));
		counters.add(new Counter("register", registerFail));
		counters.add(new Counter("delete", deleteFail));
		counters.add(new Counter("validate", validateFail));
		counters.add(new Counter("update password", updatePasswordFail));
		return counters;
	}

	private static void persistServicePass(String operation) {
		WebTarget webTarget = client.target(Constants.getMonitorAPI()).path("log").path("pass");

		Form form = new Form();
		form.param("service", "user"); // unique for each service
		form.param("operation", operation);

		webTarget.request(MediaType.APPLICATION_JSON).post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED),
				Response.class);
	}

	private static void persistServiceFail(String operation) {
		WebTarget webTarget = client.target(Constants.getMonitorAPI()).path("log").path("fail");

		Form form = new Form();
		form.param("service", "user"); // unique for each service
		form.param("operation", operation);

		webTarget.request(MediaType.APPLICATION_JSON).post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED),
				Response.class);

	}

	public static void reset() {
		searchPass = 0;
		searchFail = 0;
		registerPass = 0;
		registerFail = 0;
		deletePass = 0;
		deleteFail = 0;
		getPass = 0;
		getFail = 0;
		updatePasswordPass = 0;
		updatePasswordFail = 0;
		payCount = 0;
		validatePass = 0;
		validateFail = 0;
	}
}
