/**
 * 
 */
package path.wiser.mobile.services;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

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
	private final static String	WP_URL					= "http://wiserpath-dev.bus.ualberta.ca";	// login
	private final static String	LOGIN_PATH				= "/user/login";
	private final static String	SIGNUP_PATH				= "/user/register";

	private final static int	SUCCESS_LOGIN_CODE		= 302;										// if all went well
																									// the
	private static final int	SUCCESS_REGISTER_CODE	= 302;										// redirected to the
																									// search window
	private static final int	SERVER_UNREACHABLE		= 400;										// If Wiser Path is
																									// offline

	// page
	// redirected to your
	// account page.
	private static Credential	credential				= null;

	private static HTTPService	thisService				= null;

	private HTTPService() // block users creating this object explicitly
	{
	}

	/**
	 * @param userName
	 * @param password
	 * @return An HTTPService object that you can use to send messages to WP with.
	 */
	public static HTTPService login( String userName, String password )
	{

		// build URL to contact
		Post response = null;
		HTTPService.credential = new Credential( userName, password );
		try
		{
			URL url = HTTPService.getLoginURL();
			response = HTTPService.getLoginResponse( url );
			if (response.getReturnCode() == SUCCESS_LOGIN_CODE)
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
		// if everything succeeded then create the instance of this service and return a reference to the caller.
		if (thisService == null)
		{
			thisService = new HTTPService();
		}
		return thisService;
	}

	/**
	 * Call this if you have never used WP before and want to get started. The user will not get a HTTPService object to
	 * perform any actions on WiserPath but they can just login after they get their password from email.
	 * 
	 * @param userName must be less than 60 characters (WiserPath form limitation).
	 * @param email email of the user must be less than 64 characters.
	 * @return TODO
	 */
	public static boolean signUp( String userName, String email )
	{
		// build URL to contact
		Post response = null;
		try
		{
			URL url = HTTPService.getRegisterURL();
			response = HTTPService.getRegisterResponse( url, userName, email );
			if (response.getReturnCode() == SUCCESS_REGISTER_CODE)
			{
				Log.i( "HTTPService: SUCCESS", "User must get their login password from their email account" );
				return true;
			}
			else
			{
				Log.e( "HTTPService: Error", "Service failed to create new account for user with STATUS code: " + response.getReturnCode() );
				return true;
			}
		}
		catch (MalformedURLException e)
		{
			Log.e( "HTTPService: error", "unable to Register because URL was malformed. Has WiserPath moved?" );
			return false;
		}
		catch (UnsupportedEncodingException e) // in case the url is unsupported
												// -- should never happen.
		{
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * @param url
	 * @return status code of the request to join WiserPath.
	 */
	private static Post getRegisterResponse( URL url, String name, String email )
	{
		String dataToPost = "name=" + URLEncoder.encode( name ) + "&mail=" + URLEncoder.encode( email )
			+ "&form_id=user_register&op=Create+new+account";
		Post response = new Post( url );
		response.post( dataToPost );
		// read the returning headers and place in a Post.
		return response;
	}

	/**
	 * @return The URL to the WiserPath Registration page.
	 * @throws MalformedURLException
	 * @throws UnsupportedEncodingException
	 */
	private static URL getRegisterURL() throws MalformedURLException, UnsupportedEncodingException
	{
		String URL = HTTPService.WP_URL + HTTPService.SIGNUP_PATH;
		return new URL( URL );
	}

	/**
	 * Opens a connection and submits the users credentials and reads the
	 * response from the server and reports it.
	 * 
	 * @param url
	 * @param credential
	 * @return Post object.
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
		String URL = HTTPService.WP_URL + HTTPService.LOGIN_PATH;
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

	/**
	 * @param userName
	 * @return true if userName is greater than 0 characters and less than 60 (Drupal requirement).
	 */
	public static boolean isValidUserName( String userName )
	{
		return userName.length() > 0 && userName.length() <= 60;
	}

	/**
	 * @param emailAddress
	 * @return true if a cursory address is valid and false otherwise.
	 */
	public static boolean isValidEmailAddress( String emailAddress )
	{
		// do some basic checks -- drupal has a significantly more robust algorithm, and more compute power.
		// TODO finish me.
		return emailAddress.contains( "@" );
	}

}
