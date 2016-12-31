/**
 * Showcases a webservice
 */
package ws;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService(serviceName="DemoWebservice")	//renaming to public users again.
public class ExampleWs {
	
	//every method in the class is already a webMethod
	//with this annotation, we can change the public view of the method using operationName
	@WebMethod(operationName="mirrorText")
	public String getText(String string){
		return string;
	}
}
