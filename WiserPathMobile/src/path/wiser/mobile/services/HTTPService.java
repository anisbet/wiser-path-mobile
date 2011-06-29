/**
 * 
 */
package path.wiser.mobile.services;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

import path.wiser.mobile.geo.Blog;
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
	private static final String	GEOBLOG_PATH			= "/node/add/geoblog";

	private final static int	SUCCESS_LOGIN_CODE		= 302;										// if
																									// all
																									// went
																									// well
																									// the
	private static final int	SUCCESS_REGISTER_CODE	= 302;										// redirected
																									// to
																									// the
																									// search
																									// window
	private static final int	SERVER_UNREACHABLE		= 400;										// If
																									// Wiser
																									// Path
																									// is
																									// offline
	// used for logging in.
	private static final String	LOGIN_PASSWORD_PARAM	= "pass";
	private static final String	LOGIN_NAME_PARAM		= "name";

	// used for creating an account
	private static final String	CREATE_NAME_PARAM		= "name";
	private static final String	CREATE_EMAIL_PARAM		= "mail";

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

		WiserPathConnection connection = null;
		HTTPService.credential = new Credential( userName, password );
		try
		{
			URL url = HTTPService.getLoginURL();
			connection = WiserPathConnection.getInstance( url );
			connection.addFormData( LOGIN_NAME_PARAM, credential.getUserName() ); // TODO you might have to URLEncode
			connection.addFormData( LOGIN_PASSWORD_PARAM, credential.getPassword() );
			connection.addFormData( "form_id", "user_login" );
			connection.addFormData( "op", "log+in" );
			if (WiserPathConnection.getReturnCode() == SUCCESS_LOGIN_CODE)
			{
				credential.setCookie( WiserPathConnection.getTransactionCookie() );
			}
		}
		catch (MalformedURLException e)
		{
			Log.e( "HTTPService", "unable to log in because URL was malformed." );
		}
		catch (UnsupportedEncodingException e) // in case the url is unsupported
												// -- should never happen.
		{
			e.printStackTrace();
		}
		Log.i( "HTTPService", "Satus returned: " + connection.getReturnCode() );

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

		try
		{
			URL url = HTTPService.getRegisterURL();
			WiserPathConnection connection = WiserPathConnection.getInstance( url );
			connection.addFormData( CREATE_NAME_PARAM, credential.getUserName() ); // TODO you might have to URLEncode
			connection.addFormData( CREATE_EMAIL_PARAM, credential.getPassword() );
			connection.addFormData( "form_id", "user_register" );
			connection.addFormData( "op", "Create+new+account" );
			if (WiserPathConnection.getReturnCode() == SUCCESS_REGISTER_CODE)
			{
				Log.i( "HTTPService", "register request returned status: " + WiserPathConnection.getReturnCode() );
			}
		}
		catch (MalformedURLException e)
		{
			Log.e( "HTTPService: error", "unable to Register because URL was malformed. Contact WiserPath Admin." );
			return false;
		}
		return true;
	}

	/**
	 * Uploads a Blog object to WiserPath.
	 * 
	 * @param blog
	 * @return true if the blog was uploaded and false otherwise.
	 */
	public boolean uploadBlog( Blog blog )
	{

		return true;
	}

	/**
	 * @return The URL of the GeoBlog on WiserPath.
	 * @throws MalformedURLException
	 */
	private static URL getGeoBlogURL() throws MalformedURLException
	{
		String URL = HTTPService.WP_URL + HTTPService.GEOBLOG_PATH;
		return new URL( URL );
	}

	/**
	 * @return The URL to the WiserPath Registration page.
	 * @throws MalformedURLException
	 */
	private static URL getRegisterURL() throws MalformedURLException
	{
		String URL = WP_URL + SIGNUP_PATH;
		return new URL( URL );
	}

	/**
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws MalformedURLException
	 */
	private static URL getLoginURL() throws MalformedURLException, UnsupportedEncodingException
	{
		String URL = WP_URL + LOGIN_PATH;
		return new URL( URL );
	}

	/**
	 * @return True if they are a member and false otherwise
	 */
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
