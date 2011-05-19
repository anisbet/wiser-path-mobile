package path.wiser.mobile.services;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URLConnection;

/**
 * Takes a connection and reports results of the post
 * 
 * @author andrewnisbet
 * 
 */
public class WiserResponse
{

	private String				response;
	private DataOutputStream	printout;
	private DataInputStream		input;

	/**
	 * @param connection
	 */
	public WiserResponse( URLConnection connection, String data )
	{
		connection.setDoOutput( true );
		connection.setDoInput( true );
		connection.setUseCaches( false ); // get fresh data.
		// Specify the content type.
		connection.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded" );
		// Send POST output.
		try
		{
			printout = new DataOutputStream( connection.getOutputStream() );
			printout.writeBytes( data );
			printout.flush();
			printout.close();
			// Get response data.
			input = new DataInputStream( connection.getInputStream() );
			String str;
			while (null != ( ( str = input.readLine() ) ))
			{
				System.out.println( str );

			}
			input.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

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
				System.out.println( "Server HTTP version, Response code:" );
				System.out.println( value );
				System.out.print( "\n" );
			}
			else
			{
				System.out.println( name + "=" + value );
			}
		}
	}

	/**
	 * @return reponse code from returned header.
	 */
	public String getReturnCode()
	{
		return response;
	}

}
