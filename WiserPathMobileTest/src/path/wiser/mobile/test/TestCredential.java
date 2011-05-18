package path.wiser.mobile.test;

import java.io.File;

import junit.framework.TestCase;

import path.wiser.mobile.services.Credential;

public class TestCredential extends TestCase{


	public void testCredential() {
		Credential cred = new Credential();
		assertTrue(cred.isMember() == false);
		cred.setUserName("anisbet");
		cred.setPassword("leader1");
		cred.serialize();
		File file = new File(Credential.keyRing);
		assertTrue(file.exists());
	}


	public void testSerialize() {
		fail("Not yet implemented"); // TODO
	}

}
