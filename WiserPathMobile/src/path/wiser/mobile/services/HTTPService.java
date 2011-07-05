/**
 * 
 */
package path.wiser.mobile.services;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

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
	private final static String	WP_URL						= "http://wiserpath-dev.bus.ualberta.ca";	// login
	private final static String	LOGIN_PATH					= "/user/login";
	private final static String	SIGNUP_PATH					= "/user/register";
	private static final String	GEOBLOG_PATH				= "/node/add/geoblog";
	private static final String	GEOPHOTO_PATH				= "/node/add/geophoto";

	private final static int	SUCCESS_LOGIN_CODE			= 302;										// if
																										// all
																										// went
																										// well
																										// the
	private static final int	SUCCESS_REGISTER_CODE		= 302;										// redirected
																										// to
																										// the
																										// search
																										// window
	private static final int	SERVER_UNREACHABLE			= 400;										// If
																										// Wiser
																										// Path
																										// is
																										// offline
	public final static int		SUCCESS_BLOG_UPLOAD			= 200;
	// used for logging in.
	private static final String	LOGIN_PASSWORD_PARAM		= "pass";
	private static final String	LOGIN_NAME_PARAM			= "name";

	// used for creating an account
	private static final String	CREATE_NAME_PARAM			= "name";
	private static final String	CREATE_EMAIL_PARAM			= "mail";

	// page
	// redirected to your
	// account page.
	private static Credential	credential					= null;

	private static HTTPService	thisService					= null;
	public static final int		WISERPATH_MAX_IMAGE_SIZE	= 2000000;

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
		// if the user has already logged in successfully and closes the app, when she opens
		// it again WiserPathMobile will try and re login and get a 403 error because the
		// POST command in WiserPathConnection automatically includes a cookie if it has one.
		if (credential != null)
		{
			if (credential.isMember() == true && thisService != null)
			{
				return thisService;
			}
			else
			{
				credential.setCookie( null );
			}
		}
		WiserPathConnection connection = null;
		HTTPService.credential = new Credential( userName, password );
		try
		{
			URL url = HTTPService.getLoginURL();
			connection = WiserPathConnection.getInstance( url );
			connection.addFormData( LOGIN_NAME_PARAM, URLEncoder.encode( credential.getUserName(), "UTF-8" ) );
			connection.addFormData( LOGIN_PASSWORD_PARAM, URLEncoder.encode( credential.getPassword(), "UTF-8" ) );
			connection.addFormData( "form_id", "user_login" );
			connection.addFormData( "op", "log+in" );
			connection.POST();
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
		Log.i( "HTTPService", "Satus returned: " + WiserPathConnection.getReturnCode() );

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
			connection.addFormData( CREATE_NAME_PARAM, URLEncoder.encode( credential.getUserName(), "UTF-8" ) );
			connection.addFormData( CREATE_EMAIL_PARAM, URLEncoder.encode( credential.getPassword(), "UTF-8" ) );
			connection.addFormData( "form_id", "user_register" );
			connection.addFormData( "op", "Create+new+account" );
			connection.POST();
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
		catch (UnsupportedEncodingException e)
		{
			Log.e( "HTTPService: error", "The URL encoded user name is invalid." );
			return false;
		}
		return true;
	}

	/**
	 * Uploads a Blog object to WiserPath.
	 * 
	 * @param blog
	 */
	public void uploadBlog( Blog blog )
	{
		HTTPBlogMVC blogOnlineMVC = new HTTPBlogMVC( this, blog );
		blogOnlineMVC.update(); // upload the blog using content dependent criteria.
		blogOnlineMVC.change(); // set the isUploaded Flag.
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
		if (HTTPService.credential == null)
		{
			return false;
		}
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

	/**
	 * @param blog used to get the point information.
	 * @return The URL for posting a blog with the point of the blog appended as a query string.
	 */
	public URL getGeoblogURL( Blog blog )
	{
		try
		{
			// looks like:
			// "http://wiserpath-dev.bus.ualberta.ca/node/add/geoblog?location=POINT(-12661258.674479%207092974.0387356)"
			// return new URL( "http://wiserpath-dev.bus.ualberta.ca/node/add/geoblog?location=" + blog.getLocation() );
			return new URL( WP_URL + GEOBLOG_PATH + "?location=" + blog.getLocation() );
		}
		catch (MalformedURLException e)
		{
			Log.e( "HTTPService", "The URL for posting images is malformed. Please contact administrator." );
		}
		return null; // Could this also send the user to the HELP url?
	}

	/**
	 * @param blog
	 * @return the string version of the URL
	 */
	public URL getPostImageURL( Blog blog )
	{
		try
		{
			// return new URL( "http://wiserpath-dev.bus.ualberta.ca/node/add/geophoto?location=" + blog.getLocation()
			// );
			return new URL( WP_URL + GEOPHOTO_PATH + "?location=" + blog.getLocation() );
		}
		catch (MalformedURLException e)
		{
			Log.e( "HTTPService", "The URL for posting images is malformed. Please contact administrator." );
		}
		return null; // Could this also send the user to the HELP url?
	}

}
