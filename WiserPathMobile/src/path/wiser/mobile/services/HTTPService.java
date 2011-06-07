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
 * 
 * @author andrewnisbet
 * 
 */
public class HTTPService
{
	private final static String	LOGIN_URL	= "http://wiserpath-dev.bus.ualberta.ca";	// login
	private final static String	LOGIN_PATH	= "/user/login";
	private final static String	SIGNUP_PATH	= "/user/register";

	/**
	 * If the person has no credential saved create a new one based on a login
	 * attempt with the user name and password. Use this for first login or
	 * renewing credentials.
	 * 
	 * @param name
	 * @param password
	 * @return Credential of user
	 */
	public boolean login( Credential credential )
	{

		// build URL to contact
		try
		{
			URL url = getLoginURL();
			Log.e( "HTTPService: test", "URL: " + url.toString() ); // TODO testing remove for production.
			Post response = getLoginResponse( url, credential );
			// check response codes to ensure it worked.
			credential.setCookie( response.getWiserCookie() );
		}
		catch (MalformedURLException e)
		{
			Log.e( "HTTPService: error", "unable to log in because URL was malformed." );
			return false;
		}
		catch (UnsupportedEncodingException e) // in case the url is unsupported
												// -- should never happen.
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Opens a connection and submits the users credentials and reads the
	 * response from the server and reports it.
	 * 
	 * @param url
	 * @param credential
	 * @return
	 */
	private Post getLoginResponse( URL url, Credential credential )
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
	private URL getLoginURL() throws MalformedURLException, UnsupportedEncodingException
	{
		String URL = HTTPService.LOGIN_URL + HTTPService.LOGIN_PATH;
		return new URL( URL );
	}

}
