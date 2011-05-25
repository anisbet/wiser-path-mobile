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
	 * @param loginCredential
	 */
	private HTTPService()
	{
		/* prevent user from instantiating this class as stand alone object. */
	}

	/**
	 * If the person has no credential saved create a new one based on a login
	 * attempt with the user name and password. Use this for first login or
	 * renewing credentials.
	 * 
	 * @param name
	 * @param password
	 * @return Credential of user
	 */
	public final static Credential login( String name, String password )
	{
		Credential credential = new Credential();
		credential.setUserName( name );
		credential.setPassword( password );

		// build URL to contact
		try
		{
			URL url = getLoginURL();
			Post response = getLoginResponse( url, credential );
			// check response codes to ensure it worked.
			credential.setCookie( response.getWiserCookie() );
		}
		catch (MalformedURLException e)
		{
			Log.w( "HTTPService: error", "unable to log in because URL was malformed." );
		}
		catch (UnsupportedEncodingException e) // in case the url is unsupported
												// -- should never happen.
		{
			e.printStackTrace();
		}
		return credential;
	}

	/**
	 * Opens a connection and submits the users credentials and reads the
	 * response from the server and reports it.
	 * 
	 * @param url
	 * @param credential
	 * @return
	 */
	private static Post getLoginResponse( URL url, Credential credential )
	{
		// create the login url.
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
		return new URL( URLEncoder.encode( URL, "UTF-8" ) );
	}

}
