package service;

import java.io.IOException;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ejb.TransactionEJB;
import ejb.UserEJB;
import entity.Transaction;
import entity.User;
import utility.Constants;
import utility.ServiceAccessCounter;

@Path("/payment")
@Stateless
public class PaymentService {

	@Inject
	UserEJB userEJB;

	@Inject
	TransactionEJB transEJB;

	Client client = ClientBuilder.newClient();
	ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * When called the service will allow for payment between accounts Try to
	 * use another REST service to accomplish this one. Specifically the GetUser
	 * service.
	 * 
	 * Find both users on the DB Debit the sender for the amount Credit the
	 * receiver for the amount Save both users to the DB.
	 * 
	 * @param sender
	 * @param receiver
	 * @param amount
	 * @return
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	@POST
	@Path("/pay")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response pay(@FormParam("sender") String sender, @FormParam("receiver") String receiver,
			@FormParam("amount") double amount){

		Client client = ClientBuilder.newClient();
		ObjectMapper objectMapper = new ObjectMapper();

		// get users from the DB
		if (sender != null && receiver != null && !sender.isEmpty() && !receiver.isEmpty() && amount > 0) {
			User sender_usr = new User();
			User receiver_usr = new User();

			try{
				// get sender
				WebTarget webTarget = client.target(Constants.USER_API).path("get").path(sender);
				Response response = webTarget.request(MediaType.APPLICATION_JSON).get();
				String result = response.readEntity(String.class);
				sender_usr = objectMapper.readValue(result, User.class);

				// get receiver
				webTarget = client.target(Constants.USER_API).path("get").path(receiver);
				response = webTarget.request(MediaType.APPLICATION_JSON).get();
				result = response.readEntity(String.class);
				receiver_usr = objectMapper.readValue(result, User.class);

				// update classes
				sender_usr.updateBalance(amount, false); // credit
				receiver_usr.updateBalance(amount, true); // debit

				// save the users
				userEJB.saveUser(sender_usr);
				userEJB.saveUser(receiver_usr);

				System.out.println(sender_usr);
				System.out.println(receiver_usr);
				String msg = String.format("Successfully paid %s with %.2f", receiver, amount);
				logTransaction(sender, receiver, amount);
				// log the service access
				ServiceAccessCounter.servicePass("pay");

				return Response.ok(msg).build();
			}catch(Exception e){
				// something went wrong when trying to parse the response
				System.out.println("Dead");
				ServiceAccessCounter.serviceFail("pay");
				return Response.ok("Could not find specified users").build();
			}
			
		}

		// error in given input
		ServiceAccessCounter.serviceFail("pay");

		return Response.ok("Please enter details").build();
	}

	@GET
	@Path("/history/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTransactionHistory(@PathParam("username") String username){
		List<Transaction> transactions = transEJB.getTransactions(username);
		
		GenericEntity<List<Transaction>> entity = new GenericEntity<List<Transaction>>(transactions){
		};
		
		if(transactions.size() > 0 ){
			ServiceAccessCounter.servicePass("transaction history");
			return Response.ok(transactions).build();	
		}else{
			ServiceAccessCounter.serviceFail("transaction history");
			return Response.ok("No transaction history found for user").build();
		}
	}

	/**
	 * send to DB
	 * 
	 * Simple
	 * 
	 * @param sender
	 * @param receiver
	 * @param amount
	 */
	private void logTransaction(String sender, String receiver, double amount) {
		// TODO log the payment transaction to a table
		Transaction transaction = new Transaction(sender, receiver, amount);
		transEJB.saveTransaction(transaction);
	}
}
