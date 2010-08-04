/**
 * 
 */
package path.wiser.mobile.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import path.wiser.mobile.util.WiserHttpResponse.ResponseType;
import android.util.Log;

/**
 * @author anisbet
 *         This class manages the communication to and from a website specified
 *         by the LOGIN_PATH in the
 *         constructor or by supplying different URIs via the setURI() method.
 */
public class HttpManager
{
	public final static String	HOST			= "wiserpath.bus.ualberta.ca";
	private URI					uri				= null;
	private WiserHttpResponse	wiserResponse	= new WiserHttpResponse();
	private String[]			cookies			= null;
	public final static String	EMPTY			= "[]";

	/**
	 * @return the cookies from the last transaction
	 */
	public String[] getCookies()
	{
		return cookies;
	}

	/**
	 * @param path Constructs a {@link HttpManager} object using the default
	 *        domain and
	 *        argument path to form a URL.
	 */
	public HttpManager( String path )
	{
		// this version of android SDK has reported problems with IPv6. One
		// suggestion was to set this value false.
		System.setProperty( "java.net.preferIPv6Addresses", "false" );
		try
		{
			uri = new URI( "http", HOST, path, null );
		}
		catch (URISyntaxException e)
		{
			wiserResponse.setStatus( ResponseType.FAIL_INVALID_URI );
			Log.e( "HttpManager:Constructor", uri.toString() + e.toString() );
		}

		wiserResponse.setStatus( ResponseType.OK );
	}

	/**
	 * 
	 * @param strings
	 * @return wiserResponse with message and status.
	 */
	public HttpResponse post( String[] strings )
	{
		HttpPost httpost = new HttpPost( uri );
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpResponse response = null;
		HttpEntity entity = null;

		try
		{
			httpost.setEntity( new UrlEncodedFormEntity( getPairs( strings ), HTTP.UTF_8 ) );
		}
		catch (UnsupportedEncodingException e)
		{
			Log.i( "HttpManager", e.toString() );
		}

		try
		{
			response = httpClient.execute( httpost );
		}
		catch (Exception e)
		{
			Log.i( "HttpManager", e.toString() );
		}

		entity = response.getEntity();

		if (entity != null)
		{
			try
			{
				entity.consumeContent();
			}
			catch (IOException e)
			{
				Log.i( "LoginManager", e.toString() );
			}
		}

		setCookies( httpClient.getCookieStore().getCookies() );

		// When HttpClient instance is no longer needed,
		// shut down the connection manager to ensure
		// immediate deallocation of all system resources
		httpClient.getConnectionManager().shutdown();

		return response;
	}

	/**
	 * Sets the cookies from the last transaction.
	 * 
	 * @param cookies
	 */
	private void setCookies( List<Cookie> cookies )
	{
		if (cookies.isEmpty())
		{
			this.cookies = new String[1];
			this.cookies[0] = EMPTY;
		}
		else
		{
			this.cookies = new String[cookies.size()];
			for (int i = 0; i < cookies.size(); i++)
			{
				this.cookies[i] = cookies.get( i ).toString();
			}
		}
	}

	/**
	 * Parses out the array of strings into name value pairs and returns the
	 * array of {@link NameValuePair}s.
	 * 
	 * @param string
	 * @return nameValuePairs used to post to a server using the POST command.
	 */
	private List<NameValuePair> getPairs( String[] string )
	{
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		if (string.length == 0)
		{
			Log.e( "HttpManager", "0 length name value pair string." );
			return nameValuePairs;
		}
		if (string.length % 2 != 0)
		{
			throw new IllegalArgumentException( "Mismatch name value pair. Too many names not enough values." );
		}
		// snag out each name and value and make a pair.
		for (int i = 0; i < string.length; i += 2)
		{
			nameValuePairs.add( new BasicNameValuePair( string[i], string[i + 1] ) );
		}

		return nameValuePairs;
	}

}
