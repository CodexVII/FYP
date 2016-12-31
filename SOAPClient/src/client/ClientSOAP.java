package client;

import ws.DemoWebservice;
import ws.ExampleWs;

public class ClientSOAP {
	public static void main(String[] args){
		ExampleWs exampleWs = new DemoWebservice().getExampleWsPort();	//returns the web service created in SOAPWS project
		
		System.out.println(exampleWs.mirrorText("Whoaaa"));
		
	}
}
