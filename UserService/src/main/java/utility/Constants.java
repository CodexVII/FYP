package utility;

import java.net.InetAddress;

public class Constants {
	public static String getMonitorAPI(){
		try{
			String host = InetAddress.getLocalHost().getHostAddress();
			return "http://" + host + "/APIMonitorService/rest/monitoring";
		}catch(Exception e){
			e.printStackTrace();
			return "";
		}
	}
}
