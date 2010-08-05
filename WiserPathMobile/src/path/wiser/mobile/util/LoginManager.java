/**
 * 
 */
package path.wiser.mobile.util;

import java.util.List;

import org.apache.http.cookie.Cookie;

import android.util.Log;

/**
 * @author andrew nisbet
 * 
 *         This class tests if the user is a valid Wiser Path user
 *         and manages the user login and authentication with the web site.
 * 
 * 
 */
public class LoginManager
{

	public final static String	LOGIN_PATH		= "/user/login";
	public final static String	LOGIN_NAME		= "name";
	public final static String	LOGIN_PASSWORD	= "pass";
	private HttpManager			httpManager		= null;

	public LoginManager()
	{
		httpManager = new HttpManager( LOGIN_PATH );
		String[] args =
		{ LOGIN_NAME, "anisbet", LOGIN_PASSWORD, "WiserPathPassword_1234" };

		System.out.println( "Login form post status VALUE = " + httpManager.post( args ) );
		List<Cookie> cookies = httpManager.getCookies();

		Log.i( "LoginManager", cookies.toString() );

		// This deallocates resources after your done with the httpManager.
		httpManager.finish();

	}
}
