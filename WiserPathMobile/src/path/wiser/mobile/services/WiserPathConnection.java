package path.wiser.mobile.services;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.ContentHandler;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.Connection;

/**
 * Takes a connection and reports results of the post
 * 
 * @author andrewnisbet
 * 
 */
public class WiserPathConnection
{

	public static int login( URL url, String content )
	{
		int response = -1; // set an unusual and conspicuous number for reality check.
		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true); // sets to POST
			
			connection.setChunkedStreamingMode(0); // so we don't exhaust the buffer and overly delay transmission.
			connection.setInstanceFollowRedirects(false);
			connection.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded" );
			connection.connect();
			OutputStream out = new BufferedOutputStream( connection.getOutputStream());
			out.write(content.getBytes("UTF-8"));
			out.flush();
			out.close();
			// now read the results.
			InputStream in = new BufferedInputStream( connection.getInputStream() );
			System.out.println("=>" + readStream(in));
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
	 * Requires an open connection.
	 * 
	 * @param connection
	 */
	public static String get( URL url )
	{
		System.out.println( "Starting..." );
		String htmlPage = new String();
		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection) url.openConnection();
			connection.setInstanceFollowRedirects(false);
			InputStream in = connection.getInputStream();
			htmlPage = readStream(in);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		connection.disconnect();
		return htmlPage;
	}

	/**
	 * @param in The stream to read from.
	 * @return String of the content read from the URL.
	 */
	private static String readStream(InputStream in) 
	{
		if ( in == null )
		{
			System.out.println( "The input stream is null. Nothing to read." );
			return "";
		}
		
		char[] buffer = new char[4096];
		Writer writer = new StringWriter();
		BufferedReader reader;
		try {
			reader = new BufferedReader( new InputStreamReader( in, "UTF-8" ));
			int bytes = 0;
			while ((bytes = reader.read(buffer)) != -1)
			{
				writer.write(buffer, 0, bytes);
			}
		} 
		catch ( UnsupportedEncodingException e ) 
		{
			e.printStackTrace();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return writer.toString();
	}

}
