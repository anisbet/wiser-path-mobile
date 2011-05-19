package path.wiser.mobile.test;

import junit.framework.TestCase;

import org.junit.Test;

import path.wiser.mobile.services.Credential;
import path.wiser.mobile.services.Credential.Status;
import path.wiser.mobile.services.HTTPService;

public class TestHTTPService extends TestCase {

	@Test
	public void testHTTPService() {
		Credential cred = new Credential();
		cred.setUserName("anisbet");
		cred.setPassword("leader1");
		//cred.setUserStatus(Status.MEMBER);
		//HTTPService service = new HTTPService(cred);
	}

}
