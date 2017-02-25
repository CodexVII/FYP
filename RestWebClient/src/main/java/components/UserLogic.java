/**
 * Separating the logic for User.
 * Moves all service requests to the REST API to this class.
 * 
 */
package components;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import bean.BenchmarkFormBean;
import bean.CreateUserFormBean;
import bean.DeleteUserFormBean;
import bean.GetUserFormBean;
import bean.LoginFormBean;
import bean.PayUserFormBean;
import bean.SearchUserFormBean;
import bean.TransactionHistoryFormBean;
import bean.UpdateUserPasswordFormBean;
import core.Transaction;
import core.User;
import utility.BenchmarkManager;
import utility.Constants;

@ManagedBean(name = "userLogic")
@SessionScoped
public class UserLogic {
	private Client client = ClientBuilder.newClient(); // REST client
	private ObjectMapper objectMapper = new ObjectMapper(); // Jackson

	// MUST BE STATIC TO RETAIN DATA WHEN USED AGAIN
	private static BenchmarkManager[] bm = new BenchmarkManager[5]; // used in
																	// benchmarking

	// Injecting Beans into this one
	@ManagedProperty(value = "#{createUserForm}")
	private CreateUserFormBean createUserForm;

	@ManagedProperty(value = "#{deleteUserForm}")
	private DeleteUserFormBean deleteUserForm;

	@ManagedProperty(value = "#{getUserForm}")
	private GetUserFormBean getUserForm;

	@ManagedProperty(value = "#{searchUserForm}")
	private SearchUserFormBean searchUserForm;

	@ManagedProperty(value = "#{updateUserPasswordForm}")
	private UpdateUserPasswordFormBean updateUserPasswordForm;

	@ManagedProperty(value = "#{payUserForm}")
	private PayUserFormBean payUserForm;

	@ManagedProperty(value = "#{loginForm}")
	private LoginFormBean loginForm;

	@ManagedProperty(value = "#{benchmarkForm}")
	private BenchmarkFormBean benchmarkForm;

	@ManagedProperty(value = "#{transactionHistoryForm}")
	private TransactionHistoryFormBean transactionHistoryForm;

	// search results
	private List<User> matchedUsers;
	private User matchedUser;
	private List<Transaction> transactions;

	public List<Transaction> getTransactions() {
		return transactions;
	}
	
	public User getMatchedUser() {
		return matchedUser;
	}

	public void setMatchedUser(User matchedUser) {
		this.matchedUser = matchedUser;
	}

	public List<User> getMatchedUsers() {
		return matchedUsers;
	}

	public void setMatchedUsers(List<User> matchedUsers) {
		this.matchedUsers = matchedUsers;
	}


	/**
	 * Required to make the injection successful
	 * 
	 * @param userBean
	 */
	public void setTransactionHistoryForm(TransactionHistoryFormBean transactionHistoryForm) {
		this.transactionHistoryForm = transactionHistoryForm;
	}

	public void setCreateUserForm(CreateUserFormBean createUserForm) {
		this.createUserForm = createUserForm;
	}

	public void setDeleteUserForm(DeleteUserFormBean deleteUserForm) {
		this.deleteUserForm = deleteUserForm;
	}

	public void setGetUserForm(GetUserFormBean getUserForm) {
		this.getUserForm = getUserForm;
	}

	public void setSearchUserForm(SearchUserFormBean searchUserForm) {
		this.searchUserForm = searchUserForm;
	}

	public void setUpdateUserPasswordForm(UpdateUserPasswordFormBean updateUserPasswordForm) {
		this.updateUserPasswordForm = updateUserPasswordForm;
	}

	public void setPayUserForm(PayUserFormBean payUserForm) {
		this.payUserForm = payUserForm;
	}

	public void setLoginForm(LoginFormBean loginForm) {
		this.loginForm = loginForm;
	}

	public void setBenchmarkForm(BenchmarkFormBean benchmarkForm) {
		this.benchmarkForm = benchmarkForm;
	}

	/**
	 * Call the REST service to search for the user Store result in an array of
	 * Returns users to let JSF handle what to display instead
	 * 
	 * Users
	 * 
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	public void searchMultipleUsers() throws JsonParseException, JsonMappingException, IOException {
		System.out.println("Search was requested");
		List<User> users = new ArrayList<User>();

		String searchPattern = searchUserForm.getSearchPattern();
		// Only call the service if the @PathParam is not empty.
		if (searchPattern != null && !searchPattern.isEmpty()) {
			WebTarget webTarget = client.target(Constants.USER_API).path("search").path(searchPattern);

			Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
			Response response = invocationBuilder.get();

			// Place the JSON result in an array of User class which can be
			// accessed easily
			// toString method not adequate!
			String result = response.readEntity(String.class);
			users = objectMapper.readValue(result, new TypeReference<List<User>>() {
			});
		}
		matchedUsers = users;
	}

	public void getTransactionHistory() throws JsonParseException, JsonMappingException, IOException {
		List<Transaction> history = new ArrayList<Transaction>();

		String user = transactionHistoryForm.getUsername();
		if (user != null && !user.isEmpty()) {
			WebTarget webTarget = client.target(Constants.PAYMENT_API).path("history").path(user);

			Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
			Response response = invocationBuilder.get();

			String result = response.readEntity(String.class);
			history = objectMapper.readValue(result, new TypeReference<List<Transaction>>() {
			});
		}
		transactions = history;
	}

	/**
	 * Simply call the REST service with the user. How to send info? Can pass
	 * everything through URI as usual.. security issue?
	 * 
	 * User Jersey Form and send via APPLICATION_FORM_URL_ENCODED
	 * 
	 * Could send as JSONObject instead
	 */
	public void updatePassword() {
		WebTarget webTarget = client.target(Constants.USER_API).path("update").path("password");

		// build form data
		Form form = new Form();
		form.param("name", updateUserPasswordForm.getUsername());
		form.param("old_pwd", updateUserPasswordForm.getCurrentPassword());
		form.param("new_pwd", updateUserPasswordForm.getNewPassword());

		// send to REST service
		// read response
		Response response = webTarget.request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), Response.class);
		String result = response.readEntity(String.class);

		// add the result to the bean.
		updateUserPasswordForm.setRequestResult(result);

		// update the message component for the form
		updateUserPasswordForm.feedback();
	}

	/**
	 * Adds user to the database
	 * 
	 * 
	 * Create a form using data taken from UserBean Send to REST service
	 * 
	 * Store result of the action
	 */
	public void addUser() {
		System.out.println("Register was requested");
		WebTarget webTarget = client.target(Constants.USER_API).path("add");

		// build form data
		Form form = new Form();
		form.param("name", createUserForm.getUsername());
		form.param("password", createUserForm.getPassword());

		// send form to REST service
		Response response = webTarget.request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), Response.class);
		String result = response.readEntity(String.class);

		// set result string
		createUserForm.setRequestResult(result);

		// get feedback
		createUserForm.feedback();
	}

	public void deleteUser() {
		System.out.println("Delete was requested");
		WebTarget webTarget = client.target(Constants.USER_API).path("delete");

		Form form = new Form();
		form.param("name", deleteUserForm.getUsername());

		Response response = webTarget.request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), Response.class);
		String result = response.readEntity(String.class);

		deleteUserForm.setRequestResult(result);

		deleteUserForm.feedback();
	}

	/**
	 * Get a single user Much like the search pattern one, a test must be made
	 * to ensure that the user being searched is not null
	 * 
	 * ajax has a bad habit of running without even clicking the submit button.
	 * (reason is async maybe?)
	 * 
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public void searchSingleUser() throws JsonParseException, JsonMappingException, IOException {
		System.out.println("Single user search requested");
		String username = getUserForm.getUsername();

		if (username != null && !username.isEmpty()) {
			WebTarget webTarget = client.target(Constants.USER_API).path("get").path(username);

			Response response = webTarget.request(MediaType.APPLICATION_JSON).get();

			String result = response.readEntity(String.class);
			User tmpMatched = objectMapper.readValue(result, User.class);

			// verify that the user returned in valid
			if (tmpMatched.isValid()) {
				matchedUser = tmpMatched;
			} else {
				matchedUser = null;
			}
		} else {
			matchedUser = null;
		}

	}

	/**
	 * Call the payUser REST service with the form parameters provided by the
	 * PayUserFormBean class.
	 * 
	 * Requires: sender (String) receiver (String) amount (double)
	 */
	public void payUser() {
		WebTarget webTarget = client.target(Constants.PAYMENT_API).path("pay");

		// build form data
		Form form = new Form();
		form.param("sender", payUserForm.getSender());
		form.param("receiver", payUserForm.getReceiver());
		form.param("amount", String.valueOf(payUserForm.getAmount()));

		// send request
		Response response = webTarget.request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), Response.class);

		// recover response status
		String result = response.readEntity(String.class);
		payUserForm.setRequestResult(result);

		payUserForm.feedback();
	}

	/**
	 * Using built in Login service in the Servlet
	 * http://docs.oracle.com/javaee/6/tutorial/doc/glxce.html Allows for the
	 * use of realms to restrict viewing of pages on the site without logging
	 * in.
	 * 
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	public String login() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

		// attempt to login
		// redirect if successful

		try {
			// successful login
			request.login(loginForm.getUsername(), loginForm.getPassword());
			return "/index_3.xhtml?faces-redirect=true";
		} catch (ServletException e) {
			// failed login
			FacesMessage msg = new FacesMessage("Failed to log user in");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			context.addMessage(null, msg);
			return "/loginerror.xhtml?faces-redirect=true";
		}

	}

	/**
	 * Taken from oracle docs; same as login()
	 * http://docs.oracle.com/javaee/6/tutorial/doc/glxce.html
	 */
	public void logout() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		try {
			request.logout();
			FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
		} catch (ServletException e) {
			FacesMessage msg = new FacesMessage("Failed to logout");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			context.addMessage(null, msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Used to redirect from the login page if the user is already authorised by
	 * checking if user principle is null
	 */
	public void assertAuthorized() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

		// redirect if user is authorised
		if (request.getUserPrincipal() != null) {
			try {
				context.getExternalContext().redirect("index_3.xhtml");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Ideally that ajax call made to this method on each page call
	 * 
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	public void pollUserDetails() {
		// Call REST service to get user
		// check to see if this method was called from the same page
		if (loginForm.getUsername() != null) {
			WebTarget webTarget = client.target(Constants.USER_API).path("get").path(loginForm.getUsername());

			Response response = webTarget.request(MediaType.APPLICATION_JSON).get();

			String user_json = response.readEntity(String.class);
			User user = new User();
			try {
				user = objectMapper.readValue(user_json, User.class);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// extract user values and place into loginForm bean which
			// doubles as the core user info bean
			loginForm.setBalance(user.getBalance());

			// prepare other components that need user details
			payUserForm.setSender(loginForm.getUsername());
		}
	}

	/**
	 * Runs a stress test on the server based on the chosen service
	 * 
	 */
	public void startBenchmark() {
		// stop current benchmarks first
		stopBenchmark();

		// get selected service to benchmark
		String service = benchmarkForm.getService();

		// begin benchmark
		switch (service) {
		case "3":
			for (int i = 0; i < bm.length; i++) {
				// run search tests
				bm[i] = new BenchmarkManager();
				bm[i].setService(service);
				bm[i].start();
			}
		}
	}

	/**
	 * Stop all threads running the benchmark tests
	 */
	public void stopBenchmark() {
		System.out.println("Thread stopped #");
		for (int i = 0; i < bm.length; i++) {

			if (bm[i] != null && bm[i].getT() != null) {
				System.out.println("Thread stopped #" + i);
				bm[i].terminate();
				bm[i].getT().interrupt();
				bm[i].setT(null);
			}
		}
	}
}
