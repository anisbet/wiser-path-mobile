/**
 * 
 */
package path.wiser.mobile.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.util.Log;

/**
 * @author anisbet
 *         This class manages the communication to and from a website specified
 *         by the LOGIN_PATH in the
 *         constructor or by supplying different URIs via the setURI() method.
 */
public class HttpManager
{
	public final static String	HOST				= "wiserpath.bus.ualberta.ca";
	public final static String	EMPTY				= "[]";
	public final static int		AUTHENTICATION_PORT	= 80;

	private URI					uri					= null;
	private HttpResponse		response			= null;
	private DefaultHttpClient	httpClient			= null;

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
			Log.w( "HttpManager:Constructor", uri.toString() + e.toString() );
		}

	}

	/**
	 * 
	 * @param strings
	 * @return wiserResponse with message and status.
	 */
	public int post( String[] strings )
	{
		HttpPost httpost = new HttpPost( uri );
		httpClient = new DefaultHttpClient();

		try
		{
			httpost.setEntity( new UrlEncodedFormEntity( getPairs( strings ), HTTP.UTF_8 ) );
			response = httpClient.execute( httpost );
		}
		catch (Exception e)
		{
			Log.w( "HttpManager", e.toString() );
		}

		return response.getStatusLine().getStatusCode();
	}

	/**
	 * Authenticates a user with name and password on {@link HttpManager#HOST}
	 * and {@link HttpManager#AUTHENTICATION_PORT}.
	 * 
	 * @param name
	 * @param password
	 * @return int status of the transaction.
	 */
	public int authenticate( String name, String password )
	{
		httpClient = new DefaultHttpClient();

		httpClient.getCredentialsProvider().setCredentials( new AuthScope( HOST, AUTHENTICATION_PORT ),
			new UsernamePasswordCredentials( name, password ) );

		HttpPost httpGet = new HttpPost( uri );
		try
		{
			response = httpClient.execute( httpGet );
			System.out.println( "executing request" + httpGet.getRequestLine() );
		}
		catch (Exception e)
		{
			Log.e( "HttpManager", e.toString() );
		}

		HttpEntity entity = response.getEntity();

		if (entity != null)
		{
			try
			{
				entity.consumeContent();
			}
			catch (IOException e)
			{
				Log.e( "HttpManager", e.toString() );
			}
		}

		return response.getStatusLine().getStatusCode();
	}

	/**
	 * Releases all resources used in communication. Don't forget to call this
	 * or you will end up with a resource leak.
	 */
	public void finish()
	{
		// When HttpClient instance is no longer needed,
		// shut down the connection manager to ensure
		// immediate deallocation of all system resources
		if (httpClient != null)
		{
			httpClient.getConnectionManager().shutdown();
		}
	}

	/**
	 * @return length of the content returned by POST or GET.
	 */
	public long getContentLength()
	{
		if (response == null) return 0;
		return response.getEntity().getContentLength();
	}

	/**
	 * Returns a message in the form of a {@link HttpEntity}, or null if there
	 * was none.
	 * 
	 * @return entity message.
	 */
	public HttpEntity getMessage()
	{
		if (response == null) // happens if the method is called before a post
		{
			return null;
		}

		HttpEntity entity = response.getEntity();

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
		return entity;
	}

	/**
	 * @return String version of what ever is at the end of the URI.
	 */
	public String get()
	{
		httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet( uri );

		// Create a response handler
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String responseBody = null;
		try
		{
			responseBody = httpClient.execute( httpGet, responseHandler );
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return responseBody;
	}

	/**
	 * @return true if the underlying data associated with the
	 *         {@link HttpEntity} is chunked and false otherwise.
	 */
	public boolean isChunked()
	{
		if (response == null) return false;
		return response.getEntity().isChunked();
	}

	/**
	 * @return true if the stream is repeatable. Subsequent calls to this method
	 *         will return the same data over and over.
	 */
	public boolean isRepeatable()
	{
		if (response == null) return false;
		return response.getEntity().isRepeatable();
	}

	/**
	 * @return true if the underlying data structure of the {@link HttpEntity}
	 *         is a stream and false if otherwise. Calling getContent() method
	 *         repeatedly on a stream will throw an
	 *         {@link IllegalStateException}.
	 */
	public boolean isStreaming()
	{
		if (response == null) return false;
		return response.getEntity().isStreaming();
	}

	/**
	 * @return input stream of content of the post call.
	 */
	public InputStream getContent()
	{
		if (response == null) return null;
		try
		{
			return response.getEntity().getContent();
		}
		catch (IllegalStateException e)
		{
			Log.e( "HttpManager: you are trying to read the stream a second time.", e.toString() );
		}
		catch (IOException ioe)
		{
			Log.e( "HttpManager", ioe.toString() );
		}
		return null;
	}

	/**
	 * Mostly used for testing, this method allows you to set a new host and
	 * path.
	 * 
	 * @param host
	 * @param path
	 */
	public void setHost( String host, String path )
	{
		try
		{
			uri = new URI( "http", host, path, null );
		}
		catch (URISyntaxException e)
		{
			Log.w( "HttpManager:", uri.toString() + e.toString() );
		}
	}

	/**
	 * Returns the cookies, as Strings, from the last transaction. More
	 * specifically it returns the name and
	 * values from the cookies where name is in array index 2n and value is in
	 * array index 2n+1.
	 * 
	 * @param cookies
	 * @return List of cookies or null if the HttpClient was null.
	 */
	public List<Cookie> getCookies()
	{
		// String[] cookieStrings = null;
		// if (httpClient == null)
		// {
		// cookieStrings = new String[1];
		// cookieStrings[0] = EMPTY;
		// return cookieStrings;
		// }
		//
		// List<Cookie> cookies = httpClient.getCookieStore().getCookies();
		//
		// if (cookies.isEmpty())
		// {
		// cookieStrings = new String[1];
		// cookieStrings[0] = EMPTY;
		// return cookieStrings;
		// }
		//
		// cookieStrings = new String[cookies.size() * 2];
		//
		// for (int i = 0; i < cookies.size(); i++)
		// {
		// cookieStrings[2 * i] = cookies.get( i ).getName();
		// cookieStrings[2 * i + 1] = cookies.get( i ).getValue();
		// }
		//
		// return cookieStrings;
		if (httpClient == null) return null;
		return httpClient.getCookieStore().getCookies();
	}

	/**
	 * Parses out the array of strings into name value pairs and returns the
	 * array of {@link NameValuePair}s.
	 * 
	 * @param string
	 * @return nameValuePairs used to post to a server using the POST command.
	 */
	protected List<NameValuePair> getPairs( String[] string )
	{
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		if (string.length == 0)
		{
			Log.w( "HttpManager", "0 length name value pair string." );
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
