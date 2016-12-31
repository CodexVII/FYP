/**
 * EJB Timers in a nutshell
 */
package scheduler;

import java.util.Date;

import javax.ejb.Schedule;
import javax.ejb.Stateless;

@Stateless
public class ShowCurrentTime {
	
	// runs every second and displays the time
	// second="*/10" every 10 seconds this will be ran.
	/*
	@Schedule(second="*", minute="*", hour="*" )
	public void showTime(){
		System.out.println(new Date());
	}
	*/
}
