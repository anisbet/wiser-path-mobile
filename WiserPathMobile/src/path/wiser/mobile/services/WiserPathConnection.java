package path.wiser.mobile.services;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.io.DataOutputStream;

/**
 * Takes a connection and reports results of the post
 * 
 * @author andrewnisbet
 * 
 */
public class WiserPathConnection
{
	private static WiserPathConnection wpc;
	private static int			response	= -1;																		// set
																														// an
																														// unusual
																														// and
																														// conspicuous
																														// number
																														// for
																														// reality
																														// check.
	private static WiserCookie	wiserCookie;
	private static String		receiveContent;
	private StringBuffer		sendContent;

	public final static String	BOUNDARY	= "----WPMFormBoundary" + String.valueOf( System.currentTimeMillis() );
	private DataOutputStream contentStream;
	
	private HttpURLConnection connection; // used for multipart posts.
	
	

	/**
	 * Sets the content type to application/x-www-form-urlencoded.
	 */
	private WiserPathConnection( URL url )
	{
		receiveContent = new String();
		sendContent = new StringBuffer();
		try {
			this.connection = (HttpURLConnection) url.openConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			this.connection.setRequestMethod( "POST" );
		} catch (ProtocolException e) {
			e.printStackTrace();
		}
		this.connection.setDoOutput( true ); // sets to POST
		this.connection.setDoInput(true);
//		this.connection.setChunkedStreamingMode( 0 ); // so we don't exhaust the buffer and overly delay transmission.
		this.connection.setInstanceFollowRedirects( false );
		this.connection.setUseCaches(false);
		this.connection.setRequestProperty("Connection", "Keep-Alive");
		this.connection.setRequestProperty("Charset", "UTF-8");
		this.connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
//		this.connection.setRequestProperty("Transfer-Encoding", "chunked");
		try {
			connection.connect();
			contentStream = new DataOutputStream( connection.getOutputStream() );
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * Sets the transaction cookie. Do this immediately after creating a multipart post.
	 * 
	 * @param cookie
	 */
	public void setCookie( WiserCookie cookie )
	{
		wiserCookie = cookie;
		// if this is an instance of a multipartPost you can now set the cookie
		// if the connection has been created. Do this before sending data.
		if ( this.connection != null )
		{
			this.connection.setRequestProperty( "Cookie: ", wiserCookie.toString() );
		}
	}
	
	/**
	 * Use this constructor for a multipart post.
	 * @param url
	 * @param contentType
	 */
	public static WiserPathConnection getInstance( URL url )
	{
		if ( wpc == null )
		{
			wpc = new WiserPathConnection( url );
		}

		return wpc;
	}

	/**
	 * Allows the user to append multipart data to be sent by POST.
	 * 
	 * Use this method to sent your content type for string data like 'name="Andrew"' with a content
	 * type of FORM_DATA within a MULTIPART_FORM_DATA content type of the post over all.
	 * @param formAttrib TODO
	 * @param value
	 */
	public void addFormData( String formAttrib, String value )
	{
		if ( this.contentStream != null )
		{
			try {
				this.contentStream.writeBytes("--" +  BOUNDARY + "\r\n");
				this.contentStream.writeBytes("Content-Disposition: form-data; name=\"" + formAttrib + "\"\r\n");
				this.contentStream.writeBytes("\r\n" + value + "\r\n");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @return the HTTP status code of the response from the server.
	 */
	public int POST()
	{
		//TODO finish me. I flush and read response and close connection.
		try {
			////// For Bleep bleep sake don't forget the dashes at the end MUST have 2!!
			this.contentStream.writeBytes("--" + BOUNDARY + "--");
			this.contentStream.flush();
			this.contentStream.close();
			// now read the results.
			InputStream in;
			in = new BufferedInputStream( connection.getInputStream() );
			receiveContent = readStream( in );
			readHTTPHeader( connection, wiserCookie );
			response = connection.getResponseCode();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return response;
	}


	/**
	 * POSTs a String to a URL. This call does not support redirection.
	 * 
	 * @param url
	 * @param wiserCookie if you have one send null if not.
	 * @param receiveContent
	 * @return The HTTP status code of the transaction.
	 */
	public static int POST( URL url, String content, WiserCookie wiserCookie )
	{

		HttpURLConnection connection = null;
		try
		{
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod( "POST" );
			connection.setDoOutput( true ); // sets to POST

			connection.setChunkedStreamingMode( 0 ); // so we don't exhaust the buffer and overly delay transmission.
			connection.setInstanceFollowRedirects( false );
			connection.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded" );
			if (wiserCookie != null)
			{
				connection.setRequestProperty( "Cookie: ", wiserCookie.toString() ); // TODO if this fails try
																							// removing the space
			}
			connection.connect();
			OutputStream out = new BufferedOutputStream( connection.getOutputStream() );
			out.write( content.getBytes( "UTF-8" ) );
			out.flush();
			out.close();
			// now read the results.
			InputStream in = new BufferedInputStream( connection.getInputStream() );
			content = readStream( in );
			readHTTPHeader( connection, wiserCookie );
			response = connection.getResponseCode();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			connection.disconnect();
		}

		return response;
	}

	/**
	 * Reads the header of a connection then can be queried for values.
	 * 
	 * @param connection
	 * @param wiserCookie if null one will be made for you.
	 */
	private static void readHTTPHeader( HttpURLConnection connection, WiserCookie cookie )
	{
		if ( cookie == null )
		{
			wiserCookie = new WiserCookie();
		}
		for (int i = 0;; i++)
		{
			String name = connection.getHeaderFieldKey( i );
			String value = connection.getHeaderField( i );
			if (name == null && value == null)
			{
				break;
			}
			if (name != null)
			{
				System.out.println( name + "=" + value );
				if (name.equalsIgnoreCase( "Set-Cookie" ))
				{
					wiserCookie = new WiserCookie( value );
				}
				else
					// if this runs it is because a Location value has been set in the http header.
					if (name.equalsIgnoreCase( "Location" ))
					{
						wiserCookie.setLocation( value );
					}
			}
		}
	}

//	/**
//	 * GET sends data via the URL command line.
//	 * 
//	 * @param URL 
//	 */
//	public int GET( URL url )
//	{
//		HttpURLConnection connection = null;
//		try
//		{
//			connection = (HttpURLConnection) url.openConnection();
//			connection.setInstanceFollowRedirects( false );
//			InputStream in = connection.getInputStream();
//			receiveContent = readStream( in );
//			in.close();
//			response = connection.getResponseCode();
//		}
//		catch (IOException e)
//		{
//			e.printStackTrace();
//		}
//		finally
//		{
//			connection.disconnect();
//		}
//		return response;
//	}

	/**
	 * @param in The stream to read from.
	 * @return String of the receiveContent read from the URL.
	 */
	private static String readStream( InputStream in )
	{
		if (in == null)
		{
			System.out.println( "The input stream is null. Nothing to read." );
			return "";
		}

		char[] buffer = new char[4096];
		Writer writer = new StringWriter();
		BufferedReader reader;
		try
		{
			reader = new BufferedReader( new InputStreamReader( in, "UTF-8" ) );
			int bytes = 0;
			while (( bytes = reader.read( buffer ) ) != -1)
			{
				writer.write( buffer, 0, bytes );
			}
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return writer.toString();
	}

	/**
	 * @return The HTTP status code from the HTTP header from the return transaction.
	 */
	public static int getReturnCode()
	{
		return response;
	}

	/**
	 * @return The wiser path transaction cookie from this transaction.
	 */
	public static WiserCookie getTransactionCookie()
	{
		return wiserCookie;
	}

	/**
	 * @return the receiveContent may be empty if there was none.
	 */
	public static String getContent()
	{
		return receiveContent;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return this.sendContent.toString();
	}

}
