package path.wiser.mobile.services;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Takes a connection and reports results of the post
 * 
 * @author andrewnisbet
 * 
 */
public class Post
{

	private int				response	= -1;	// unset
	private WiserCookie		wiserCookie	= null;
	private URLConnection	connection	= null;

	/**
	 * Requires an open connection.
	 * 
	 * @param connection
	 */
	public Post( URL url )
	{
		response = 400; // preset to error and reset when another code is available.
		try
		{
			connection = url.openConnection();
		}
		catch (Exception e)
		{
			// Log.e( "Post:Error", "The URL is either NULL or there was an IO error." );
			System.err.println( "Post error: Cannot connect because the URL is either NULL or there was an IO error." );
		}
		connection.setDoOutput( true );
		connection.setDoInput( true );
		connection.setUseCaches( false ); // get fresh data.
		// Specify the content type.
		connection.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded" );
	}

	/**
	 * @param data
	 * @return String of the data returned from the post or null if there was none.
	 */
	@SuppressWarnings("deprecation")
	public String post( String data )
	{
		String result = new String();
		connection.setDoOutput( true );
		connection.setDoInput( true );
		connection.setUseCaches( false ); // get fresh data.
		// Specify the content type.
		connection.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded" );
		DataOutputStream outBoundStream = null;
		DataInputStream inBoundStream = null;
		try
		{
			outBoundStream = new DataOutputStream( connection.getOutputStream() );
			outBoundStream.writeBytes( data );
			outBoundStream.flush();
			outBoundStream.close();
			// Get response data.
			inBoundStream = new DataInputStream( connection.getInputStream() );
			String temp;
			while (( temp = inBoundStream.readLine() ) != null)
			{
				result += temp + "\n";
			}
		}
		catch (IOException e) // can be IOException or EOFException when the stream has finished reading.
		{
			// Log.e( "Post:Error", "Could not post because of an IO error." );
			System.err.println( "Post error: Could not post because of an IO error. Perhaps the server down or unreachable." );
			response = 400;
		}
		finally
		{
			try
			{
				inBoundStream.close();
				readHTTPHeader( connection );
				return result.toString();
			}
			catch (Exception e)
			{
				response = 400; // this is normal if the inbound stream wasn't opened or if there was an exception
								// during closing.
			}
		}
		return null;
	}

	/**
	 * Reads the header of a connection then can be queried for values.
	 * 
	 * @param connection
	 */
	private void readHTTPHeader( URLConnection connection )
	{
		for (int i = 0;; i++)
		{
			String name = connection.getHeaderFieldKey( i );
			String value = connection.getHeaderField( i );
			if (name == null && value == null)
			{
				break;
			}
			if (name == null)
			{
				// this retrieves out the HTTP version number and the status code.
				String[] values = value.split( " " );
				response = Integer.parseInt( values[1] );
			}
			else
			{
				// System.out.println( name + "=" + value );
				if (name.equalsIgnoreCase( "Set-Cookie" ))
				{
					wiserCookie = new WiserCookie( value );
				}
			}
		}
	}

	/**
	 * @return reponse code from returned header.
	 */
	public int getReturnCode()
	{
		return response;
	}

	/**
	 * @return the wiserCookie
	 */
	public WiserCookie getWiserCookie()
	{
		return wiserCookie;
	}

}
