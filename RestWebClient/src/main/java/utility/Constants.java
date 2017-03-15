package utility;

import java.net.InetAddress;

public class Constants {
	public static String getUserAPI(){
		try{
			String host = InetAddress.getLocalHost().getHostAddress();
			return "http://" + host + "/UserService/rest/user";
		}catch(Exception e){
			e.printStackTrace();
			return "";
		}
	}

	public static String getPaymentAPI(){
		try{
			String host = InetAddress.getLocalHost().getHostAddress();
			return "http://" + host + "/PaymentService/rest/payment";
		}catch(Exception e){
			e.printStackTrace();
			return "";
		}
	}
}
