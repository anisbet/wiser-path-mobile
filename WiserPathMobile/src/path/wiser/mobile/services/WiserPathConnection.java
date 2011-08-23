package path.wiser.mobile.services;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
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
import java.util.HashMap;

/**
 * Takes a connection and reports results of the post
 * 
 * @author andrewnisbet
 * 
 */
public class WiserPathConnection
{
	private static int						response	= -1;																	// set
																																// an
																																// unusual
																																// and
																																// conspicuous
																																// number
																																// for
																																// reality
																																// check.
	private static WiserCookie				wiserCookie;
	private static String					receiveContent;
	private static HashMap<String, String>	header;
	private static boolean					redirect	= false;																// permits
																																// or
																																// denies
																																// redirects.

	private StringBuffer					sendContent;
	public final static String				BOUNDARY	= "----WPMFormBoundary" + String.valueOf( System.currentTimeMillis() );
	private static final boolean			DEBUG		= false;

	private HttpURLConnection				connection;																		// used
																																// for
																																// multipart
																																// posts.
	private static HashMap<String, String>	specialHeaderRequests;

	/**
	 * Sets the content type to application/x-www-form-urlencoded.
	 */
	private WiserPathConnection( URL url )
	{
		receiveContent = new String();
		sendContent = new StringBuffer();
		response = -1;
		try
		{
			this.connection = (HttpURLConnection) url.openConnection();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		try
		{
			this.connection.setRequestMethod( "POST" );
		}
		catch (ProtocolException e)
		{
			e.printStackTrace();
		}
		this.connection.setDoOutput( true ); // sets to POST
		this.connection.setDoInput( true );
		// this.connection.setChunkedStreamingMode( 0 ); // so we don't exhaust the buffer and overly delay
		// transmission.
		this.connection.setInstanceFollowRedirects( redirect );
		this.connection.setUseCaches( false );
		this.connection.setRequestProperty( "Connection", "Keep-Alive" );
		this.connection.setRequestProperty( "Charset", "UTF-8" );
		if (specialHeaderRequests != null)
		{
			setAdditionalRequestProperties( this.connection ); // sets and properties for this request
		}
		if (wiserCookie != null)
		{
			this.connection.setRequestProperty( "Cookie", wiserCookie.toString() );
		}
		this.connection.setRequestProperty( "Content-Type", "multipart/form-data; boundary=" + BOUNDARY );
		// this.connection.setRequestProperty("Transfer-Encoding", "chunked");
		try
		{
			this.connection.connect();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	/**
	 * Pre: the specialHeaderRequests hashmap must not be null. Connection must not be null.
	 */
	private static void setAdditionalRequestProperties( HttpURLConnection myConnection )
	{
		java.util.Iterator<String> iterator = specialHeaderRequests.keySet().iterator();

		while (iterator.hasNext())
		{
			String request = iterator.next();
			String property = specialHeaderRequests.get( request );
			if (property != null)
			{
				myConnection.setRequestProperty( request, property );
			}
		}
		// once set we remove all the extra properties because the hashmap is a static instance and we don't necessarily
		// want
		// this properties for all posts or gets.
		specialHeaderRequests.clear();

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
		if (this.connection != null)
		{
			this.connection.setRequestProperty( "Cookie: ", wiserCookie.toString() );
		}
	}

	/**
	 * Use this constructor for a multipart post.
	 * 
	 * @param url
	 * @param contentType
	 */
	public static WiserPathConnection getInstance( URL url )
	{
		return new WiserPathConnection( url );
	}

	/**
	 * @param attribName -- the name of the field from the form.
	 * @param fileName
	 * @param path
	 */
	public void addImageData( String attribName, String fileName, String path )
	{
		File f = new File( path );
		int len = (int) f.length(); // create a buffer big enough for the image.

		try
		{
			DataOutputStream contentStream = new DataOutputStream( connection.getOutputStream() );
			contentStream.writeBytes( "--" + BOUNDARY + "\r\n" );
			contentStream.writeBytes( "Content-Disposition: form-data; name=\"" + attribName + "\"; filename=\"" + fileName + "\"" + "\r\n" );
			// TODO add more image types if necessary
			contentStream.writeBytes( "Content-Type: image/jpeg\r\n\r\n" );
			FileInputStream fStream = new FileInputStream( path );
			byte[] data = new byte[len];
			fStream.read( data );
			contentStream.write( data );
			contentStream.writeBytes( "\r\n" );
			fStream.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Adds XML content to the multi-part form.
	 * 
	 * @param attribName attribute name of the form field.
	 * @param fileName of the XML file being uploaded.
	 * @param content string.
	 */
	public void addXMLData( String attribName, String fileName, String content )
	{
		try
		{
			DataOutputStream contentStream = new DataOutputStream( connection.getOutputStream() );
			contentStream.writeBytes( "--" + BOUNDARY + "\r\n" );
			contentStream.writeBytes( "Content-Disposition: form-data; name=\"" + attribName + "\"; filename=\"" + fileName + "\"" + "\r\n" );
			// TODO add more image types if necessary
			contentStream.writeBytes( "Content-Type: application/octet-stream\r\n\r\n" );
			byte[] data = content.getBytes( "UTF-8" );
			contentStream.write( data );
			contentStream.writeBytes( "\r\n" );
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	/**
	 * Allows the user to append multipart data to be sent by POST.
	 * 
	 * Use this method to sent your content type for string data like 'name="Andrew"' with a content
	 * type of FORM_DATA within a MULTIPART_FORM_DATA content type of the post over all.
	 * 
	 * @param formAttrib TODO
	 * @param value
	 */
	public void addFormData( String formAttrib, String value )
	{
		try
		{
			DataOutputStream contentStream = new DataOutputStream( connection.getOutputStream() );
			contentStream.writeBytes( "--" + BOUNDARY + "\r\n" );
			contentStream.writeBytes( "Content-Disposition: form-data; name=\"" + formAttrib + "\"\r\n" );
			contentStream.writeBytes( "\r\n" + value + "\r\n" );
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * @return the HTTP status code of the response from the server.
	 */
	public int POST()
	{
		try
		{
			// //// For Bleep bleep sake don't forget the dashes at the end MUST have 2!!
			DataOutputStream contentStream = new DataOutputStream( connection.getOutputStream() );
			contentStream.writeBytes( "--" + BOUNDARY + "--" );
			contentStream.flush();
			contentStream.close();
			// now read the results.
			InputStream in = new BufferedInputStream( this.connection.getInputStream() );
			receiveContent = readStream( in );
			readHTTPHeader( this.connection, wiserCookie );
			response = this.connection.getResponseCode();
			in.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			this.connection.disconnect();
			this.connection = null;
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
			connection.setInstanceFollowRedirects( redirect );
			connection.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded" );
			if (specialHeaderRequests != null)
			{
				// System.out.println(
				// "==============================\n adding special property requests.\n================================="
				// );
				setAdditionalRequestProperties( connection ); // set additional properties for this POST.
			}
			if (wiserCookie != null)
			{
				// System.out.println(
				// "==============================\n POST adding cookie.\n=================================" );
				connection.setRequestProperty( "Cookie", wiserCookie.toString() );
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
		if (cookie == null)
		{
			wiserCookie = new WiserCookie();
		}
		header = new HashMap<String, String>();
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
				if (DEBUG) System.out.println( name + "=" + value );
				header.put( name, value );
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

	/**
	 * This method returns the header value stored by reference to the supplied key.
	 * 
	 * @param whichHeader
	 * @return The value stored in the header by the key (case sensitive) or null if it could not be found.
	 */
	public static String getHeaderValue( String whichHeader )
	{
		return header.get( whichHeader );
	}

	/**
	 * GET sends data via the URL command line.
	 * 
	 * @param URL
	 */
	public static int GET( URL url )
	{
		HttpURLConnection connection = null;
		receiveContent = new String();
		try
		{
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod( "GET" );
			connection.setDoOutput( true );
			connection.setDoInput( true );
			connection.setInstanceFollowRedirects( redirect );
			connection.setUseCaches( false );
			connection.setRequestProperty( "Charset", "UTF-8" );
			if (specialHeaderRequests != null)
			{
				System.out.println( "==============================\n adding special property requests.\n=================================" );
				setAdditionalRequestProperties( connection ); // sets and then clears this instances connection property
																// requests.
			}
			if (wiserCookie != null)
			{
				System.out.println( "==============================\n GET adding cookie.\n=================================" );
				connection.setRequestProperty( "Cookie", wiserCookie.toString() );
			}
			connection.connect();
			InputStream in = connection.getInputStream();
			receiveContent = readStream( in );
			in.close();
			response = connection.getResponseCode();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			// close the connection, set all objects to null
			connection.disconnect();
		}
		return response;
	}

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return this.sendContent.toString();
	}

	/**
	 * @param b true if you allow redirects and false otherwise
	 */
	public static void setAllowRedirects( boolean b )
	{
		redirect = b;
	}

	/**
	 * @param htmlPage
	 * @return The id of the form if there is one and an empty string otherwise.
	 */
	public static String getFormToken( String htmlPage )
	{

		String searchString = "name=\"form_token\"";
		int start = htmlPage.indexOf( searchString );
		if (start > 0)
		{
			String value = "value=\"";
			start = htmlPage.indexOf( value, start ) + value.length();
			int end = htmlPage.indexOf( "\"", start );
			return htmlPage.substring( start, end );
		}
		return "";

	}

	/**
	 * Currently this only effects multipart posts.
	 * Equiv to calling HttpURLConnection.setRequestProperty()
	 * 
	 * @param string
	 * @param referString
	 */
	public static void setRequestProperty( String string, String referString )
	{
		if (specialHeaderRequests == null)
		{
			specialHeaderRequests = new HashMap<String, String>();
		}
		specialHeaderRequests.put( string, referString );
	}

}
