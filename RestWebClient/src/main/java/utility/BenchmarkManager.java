package utility;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

/**
 * Thread which, when run, will begin to repeatedly requesting request a service
 * with the given parameters until stopped.
 * 
 * No need to check the response. The request is the only thing made here
 * 
 * @author keita
 *
 */
public class BenchmarkManager implements Runnable {
	private Thread t;

	private volatile boolean running = true;
	private String service;

	// API parameters
	private static String searchPattern;

	// API access
	private static final String api = Constants.API;
	private static Client client = ClientBuilder.newClient(); // REST client

	public BenchmarkManager() {
		searchPattern = "i";
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getService() {
		return service;
	}

	public static String getSearchPattern() {
		return searchPattern;
	}

	public static void setSearchPattern(String searchPattern) {
		BenchmarkManager.searchPattern = searchPattern;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public Thread getT() {
		return t;
	}

	public void setT(Thread t) {
		this.t = t;
	}

	public void search() {
		// Only call the service if the @PathParam is not empty.
		if (searchPattern != null && !searchPattern.isEmpty()) {
			WebTarget webTarget = client.target(api).path("search").path(searchPattern);

			// request service
			webTarget.request(MediaType.APPLICATION_JSON).get();
		}
	}

	/**
	 * Depending on the service called, begin the relevant benchmark
	 */
	@Override
	public void run() {
		int i = 0;

		if (service != null) {
			switch (service) {
			case "1":
				while (running) {
				}
				break;
			case "3":
				while (running) {
					search();
				}
				break;
			}
		}
	}

	/**
	 * set flag to stop requests
	 */
	public void terminate() {
		running = false;
	}

	/**
	 * Creates the thread which starts up the run() method if successful.
	 */
	public void start() {
		if (t == null) {
			t = new Thread(this);
			t.start();
		}
	}
}
