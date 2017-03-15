package bean;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import utility.Constants;
import utility.Counter;

@ManagedBean
@RequestScoped
public class CompleteServiceFail {
	private List<Counter> counters;
	private Client client = ClientBuilder.newClient(); // REST client
	private ObjectMapper objectMapper = new ObjectMapper(); // Jackson

	public List<Counter> getCounters() {
		return counters;
	}

	public void setCounters(List<Counter> counters) {
		this.counters = counters;
	}

	public CompleteServiceFail() {
		reload();
	}

	private void reload() {
		counters = new ArrayList<Counter>();

		WebTarget webTarget = client.target(Constants.getMonitorAPI()).path("counters/fail").path("user");

		Response response = webTarget.request(MediaType.APPLICATION_JSON).get();

		String result = response.readEntity(String.class);
		try {
			counters = objectMapper.readValue(result, new TypeReference<List<Counter>>() {
			});
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
