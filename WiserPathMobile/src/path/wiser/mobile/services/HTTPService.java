/**
 * 
 */
package path.wiser.mobile.services;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

import android.util.Log;

/**
 * This class manages the communication of the andoid device and WiserPath
 * services.
 * It is meant to be a singleton class. It holds a static instance of itself and
 * cannot be instantiated externally -- only through successful login. Once the
 * the user has logged onto the system successfully they can call the static method {@link#getService()} to get an
 * instance anywhere else in the code.
 * 
 * @author andrewnisbet
 * 
 */
public class HTTPService
{
	private final static String	LOGIN_URL		= "http://wiserpath-dev.bus.ualberta.ca";	// login
	private final static String	LOGIN_PATH		= "/user/login";
	private final static String	SIGNUP_PATH		= "/user/register";

	private final static int	SUCCESS_CODE	= 302;										// if all went well the page
																							// redirected to your
																							// account page.
	private static Credential	credential		= null;

	private static HTTPService	thisService		= null;

	private HTTPService()
	{
	}

	/**
	 * @param userName
	 * @param password
	 * @return An HTTPService object that you can use to send messages to WP with.
	 */
	public static HTTPService login( String userName, String password )
	{
		if (thisService == null)
		{
			thisService = new HTTPService();
		}
		// build URL to contact
		Post response = null;
		HTTPService.credential = new Credential( userName, password );
		try
		{
			URL url = HTTPService.getLoginURL();
			response = HTTPService.getLoginResponse( url );
			if (response.getReturnCode() == SUCCESS_CODE)
			{
				HTTPService.credential.setCookie( response.getWiserCookie() );
			}
		}
		catch (MalformedURLException e)
		{
			Log.e( "HTTPService: error", "unable to log in because URL was malformed." );
		}
		catch (UnsupportedEncodingException e) // in case the url is unsupported
												// -- should never happen.
		{
			e.printStackTrace();
		}
		Log.i( "HTTPService: message", "Satus returned: " + String.valueOf( response.getReturnCode() ) );
		return thisService; //
	}

	/**
	 * Opens a connection and submits the users credentials and reads the
	 * response from the server and reports it.
	 * 
	 * @param url
	 * @param credential
	 * @return
	 */
	private static Post getLoginResponse( URL url )
	{
		// create the login url. These values can be nothing if the user has no
		// preferences set.
		String dataToPost = "name=" + credential.getUserName() + "&pass=" + credential.getPassword() + "&form_id=user_login&op=log+in";
		Post response = new Post( url );
		response.post( dataToPost );
		// read the returning headers and place in a Post.
		return response;
	}

	/**
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws MalformedURLException
	 */
	private static URL getLoginURL() throws MalformedURLException, UnsupportedEncodingException
	{
		String URL = HTTPService.LOGIN_URL + HTTPService.LOGIN_PATH;
		return new URL( URL );
	}

	public boolean isLoggedIn()
	{
		return HTTPService.credential.isMember();
	}

	/**
	 * @return Null if the user has not logged in successfully and the
	 *         HTTPService object if they have.
	 */
	public static HTTPService getInstance()
	{
		return HTTPService.thisService;
	}

}
