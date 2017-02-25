package service;

import java.util.ArrayList;
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
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ejb.ServiceAccessEJB;
import entity.ServiceAccess;
import entity.ServiceAccessPK;
import utility.Counter;

@Path("/monitoring")
@Stateless
public class APIMonitorService {
	@Inject
	ServiceAccessEJB saEJB;

	@POST
	@Path("/log/fail/")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response incrementServiceFailCount(@FormParam("service") String service,
			@FormParam("operation") String operation) {
		ServiceAccess sa = saEJB.getServiceAccess(service, operation);

		// increment failure
		sa.setFail(sa.getFail() + 1);

		// save the new value
		saEJB.saveServiceAccess(sa);

		return Response.ok().build();
	}

	@POST
	@Path("/log/pass/")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response incrementServicePassCount(@FormParam("service") String service,
			@FormParam("operation") String operation) {
		ServiceAccess sa = saEJB.getServiceAccess(service, operation);

		// increment failure
		sa.setPass(sa.getPass() + 1);

		// save the new value
		saEJB.saveServiceAccess(sa);

		return Response.ok().build();
	}

	/**
	 * Return a list of all the persisted service operations
	 * 
	 * List of counters which are ready to be graphed
	 * 
	 * Scenario: User service -> I'd like a list of counters to graph the
	 * overall usage of my operators This service -> Get list of all services
	 * from DB Sort for the service that called us Create list of counters based
	 * on that service return
	 * 
	 * @param service
	 * @return
	 */
	@GET
	@Path("/counters/pass/{service}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPersistedPass(@PathParam("service") String service) {
		List<ServiceAccess> sa = saEJB.getAll();

		// pick out all of the matched service operations
		// create counter objects from them
		List<Counter> counters = new ArrayList<Counter>();
		ServiceAccess currentService;
		ServiceAccessPK currentServicePK;
		for (int i = 0; i < sa.size(); i++) {
			currentService = sa.get(i);
			currentServicePK = currentService.getId(); // service + op

			// add to the list relevant services
			if (currentServicePK.getService().toLowerCase().equals(service)) {
				// service matched, add the counter containing operation and count
				counters.add( new Counter(currentServicePK.getOperation(), currentService.getPass()));
			}
		}
		
		System.out.println("Size is" + counters.size());
		// return this ready made counter for graphing
		GenericEntity<List<Counter>> entity = new GenericEntity<List<Counter>>(counters) {
		};
		return Response.ok(entity).build();
	}
}
