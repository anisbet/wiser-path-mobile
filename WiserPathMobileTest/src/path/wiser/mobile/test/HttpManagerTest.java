package path.wiser.mobile.test;

import java.io.InputStream;
import java.util.List;

import org.apache.http.cookie.Cookie;

import android.util.Log;
import path.wiser.mobile.util.HttpManager;
import path.wiser.mobile.util.LoginManager;
import junit.framework.TestCase;

public class HttpManagerTest extends TestCase {

	public void testHttpManager() {
		HttpManager httpManager = new HttpManager("/user/login");
		assertNotNull(httpManager);
	}

	public void testPost() {
		HttpManager httpManager = new HttpManager("/user/login");
		String[] args =	{ LoginManager.LOGIN_NAME, "anisbet", LoginManager.LOGIN_PASSWORD, "WiserPathPassword_1234" };
		assertNull(httpManager.getCookies());
		httpManager.post(args);
		List<Cookie> retValues = httpManager.getCookies();
		assertNotNull(retValues);
		httpManager.finish();
	}

	public void testGetMessage() {
		HttpManager httpManager = new HttpManager("/user/login");
		// test
		httpManager.setHost("www.ualberta.ca", "/~anisbet");
		System.out.println(httpManager.get());
		assertTrue(httpManager.isStreaming());
		InputStream someStream = httpManager.getContent();
		assertNotNull(someStream);
		System.err.println(someStream);
		httpManager.finish();
	}
	
	public void testAuthenticate(){
		HttpManager httpManager = new HttpManager("/usr/login");
		System.out.println("return status from authenticate: " + httpManager.authenticate("anisbet", "WiserPathPassword_1234"));
		httpManager.finish();
	}

	public void testGetCookies() {
		HttpManager httpManager = new HttpManager("/user/login");
		String[] args =	{ LoginManager.LOGIN_NAME, "anisbet", LoginManager.LOGIN_PASSWORD, "WiserPathPassword_1234" };
		assertNull(httpManager.getCookies());
		httpManager.post(args);
		List<Cookie> retValues = httpManager.getCookies();
		assertTrue(retValues.size() >= 1);
		for (int i = 0; i < retValues.size(); i++)
		{
			System.out.println("Cookie " + i + " " + retValues.get(i).toString());
		}
		httpManager.finish();
	}

}
