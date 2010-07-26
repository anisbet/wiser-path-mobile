/**
 * 
 */
package path.wiser.mobile.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

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
	public final static String	IP				= "129.128.94.61";
	private URI					uri				= null;
	private WiserHttpResponse	wiserResponse	= new WiserHttpResponse();

	public HttpManager( String path )
	{
		try
		{
			URL url = new URL( "http", IP, path );
			uri = url.toURI();
		}
		catch (URISyntaxException e)
		{
			wiserResponse.setStatus( ResponseType.FAIL_INVALID_URI );
			Log.e( "HttpManager:Constructor", uri.toString() + e.toString() );
		}
		catch (MalformedURLException e)
		{
			wiserResponse.setStatus( ResponseType.FAIL_INVALID_URI );
			Log.e( "HttpManager:Constructor", uri.toString() + e.toString() );
		}
		wiserResponse.setStatus( ResponseType.OK );
	}

	/**
	 * 
	 * @param userName
	 * @param password
	 * @return wiserResponse with message and status.
	 */
	public HttpResponse postLogin( String userName, String password )
	{
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost( uri );
		HttpResponse response = null;
		try
		{
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
				2 );
			nameValuePairs.add( new BasicNameValuePair( "id", "edit-name" ) );
			nameValuePairs
				.add( new BasicNameValuePair( "stringdata", userName ) );
			httppost.setEntity( new UrlEncodedFormEntity( nameValuePairs ) );

			// Execute HTTP Post Request
			response = httpclient.execute( httppost );
			Log.i( "HttpManager:", "======> response: "
				+ response.getEntity().getContent() );

		}
		catch (ClientProtocolException e)
		{
			Log.e( "HttpManager", "ClientProtocolException thrown" + e );
		}
		catch (IOException e)
		{
			Log.i( "HttpManager", uri.toASCIIString() );
			Log.e( "HttpManager", "IOException thrown" + e );
		}

		return response;
	}

	/**
	 * @return the isValidUri
	 */
	public boolean isSuccessful()
	{
		return wiserResponse.isSuccessful();
	}

	/**
	 * @param destinationURI the uri to set allows the user to reset the uri for
	 *        this object and reuse the object
	 */
	public void setURI( String destinationURI )
	{
		try
		{
			uri = new URI( destinationURI );
		}
		catch (URISyntaxException e)
		{
			wiserResponse.setStatus( ResponseType.FAIL_INVALID_URI );
		}
		wiserResponse.setStatus( ResponseType.OK );
	}
}
