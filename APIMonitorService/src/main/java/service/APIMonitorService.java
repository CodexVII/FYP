package service;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ejb.ServiceAccessEJB;
import entity.ServiceAccess;

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
}
