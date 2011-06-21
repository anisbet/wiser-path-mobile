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
import java.net.URL;

/**
 * Takes a connection and reports results of the post
 * 
 * @author andrewnisbet
 * 
 */
public class WiserPathConnection
{
	private int					response	= -1;																		// set
																														// an
																														// unusual
																														// and
																														// conspicuous
																														// number
																														// for
																														// reality
																														// check.
	private WiserCookie			wiserCookie;
	private String				receiveContent;
	private StringBuffer		sendContent;
	private String				contentType;

	public final static String	BOUNDARY	= "-------WPMFormBoundary" + String.valueOf( System.currentTimeMillis() );
	private String				boundary	= "\r\n" + BOUNDARY + "\r\n";
	private OutputStream		contentStream;

	public enum ContentType
	{
		X_WWW_FORM_URLENCODED, MULTIPART_FORM_DATA, IMAGE_JPEG, FORM_DATA
	}

	/**
	 * @param contentType of the overall post.
	 */
	public WiserPathConnection( ContentType contentType )
	{
		receiveContent = new String();
		this.contentType = setContentType( contentType );
	}

	/**
	 * Sets the content type to application/x-www-form-urlencoded.
	 */
	public WiserPathConnection()
	{
		receiveContent = new String();
		contentType = setContentType( ContentType.X_WWW_FORM_URLENCODED );
	}

	/**
	 * Sets the transaction cookie.
	 * 
	 * @param cookie
	 */
	public void setCookie( WiserCookie cookie )
	{
		this.wiserCookie = cookie;
	}

	/**
	 * @param contentType for post data.
	 */
	private String setContentType( ContentType contentType )
	{
		switch (contentType)
		{
		case MULTIPART_FORM_DATA:
			return "multipart/form-data; boundary=" + BOUNDARY;
			// this.contentType = "multipart/form-data";
		case IMAGE_JPEG:
			return "image/jpeg";
		case FORM_DATA:
			return "Content-Disposition: form-data; ";
			// this.contentType = "multipart/form-data";
		default:
			return "application/x-www-form-urlencoded";
		}
	}

	/**
	 * Allows the user to append multipart data to be sent by POST.
	 * 
	 * Use this method to sent your content type for string data like 'name="Andrew"' with a content
	 * type of FORM_DATA within a MULTIPART_FORM_DATA content type of the post over all.
	 * 
	 * @param type
	 * @param content
	 */
	public void setMultiPartContent( ContentType type, String content )
	{
		// don't do anything if the user specified any other content type -- they may be confused about types.
		switch (type)
		{
		case FORM_DATA:
			this.sendContent.append( type );
			this.sendContent.append( content );
			this.sendContent.append( this.boundary );
			break;
		default:
			break;
		}

	}

	/**
	 * Allows the user to append multipart data to be sent by POST.
	 * 
	 * Use this method to sent your content type for string data like 'name="files[field_photo_0]";
	 * filename="spitz.jpg"' with a content
	 * type of FORM_DATA within a MULTIPART_FORM_DATA content type of the post over all.
	 * 
	 * @param type
	 * @param content
	 */
	public void setMultiPartContent( ContentType type, String content, OutputStream contentStream )
	{
		// don't do anything if the user specified any other content type -- they may be confused about types.
		switch (type)
		{
		case IMAGE_JPEG:
			this.sendContent.append( setContentType( ContentType.FORM_DATA ) );
			this.sendContent.append( content + "\r\n" );
			this.sendContent.append( setContentType( ContentType.IMAGE_JPEG ) + "\r\n" );
			this.sendContent.append( this.boundary );
			this.contentStream = contentStream;
			break;
		default:
			break;
		}

	}

	/**
	 * Use this method if you have set multi-part data in with the {@link#setContent()} method.
	 * 
	 * @param url to post the data to.
	 * @return HTTP status
	 */
	public int POST( URL url )
	{
		return POST( url, this.sendContent.toString() );
	}

	/**
	 * POSTs a String to a URL. This call does not support redirection.
	 * 
	 * @param url
	 * @param receiveContent
	 * @return The HTTP status code of the transaction.
	 */
	public int POST( URL url, String content )
	{

		HttpURLConnection connection = null;
		try
		{
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod( "POST" );
			connection.setDoOutput( true ); // sets to POST

			connection.setChunkedStreamingMode( 0 ); // so we don't exhaust the buffer and overly delay transmission.
			connection.setInstanceFollowRedirects( false );
			connection.setRequestProperty( "Content-Type", contentType );
			if (this.wiserCookie != null)
			{
				connection.setRequestProperty( "Cookie: ", this.wiserCookie.toString() ); // TODO if this fails try
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
			readHTTPHeader( connection );
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
	 */
	private void readHTTPHeader( HttpURLConnection connection )
	{
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
					this.wiserCookie = new WiserCookie( value );
				}
				else
					// if this runs it is because a Location value has been set in the http header.
					if (name.equalsIgnoreCase( "Location" ))
					{
						this.wiserCookie.setLocation( value );
					}
			}
		}
	}

	/**
	 * GET sends data via the URL command line.
	 * 
	 * @param connection
	 */
	public int get( URL url )
	{
		HttpURLConnection connection = null;
		try
		{
			connection = (HttpURLConnection) url.openConnection();
			connection.setInstanceFollowRedirects( false );
			InputStream in = connection.getInputStream();
			this.receiveContent = readStream( in );
			in.close();
			this.response = connection.getResponseCode();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			connection.disconnect();
		}
		return this.response;
	}

	/**
	 * @param in The stream to read from.
	 * @return String of the receiveContent read from the URL.
	 */
	private String readStream( InputStream in )
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
	public int getReturnCode()
	{
		return this.response;
	}

	/**
	 * @return The wiser path transaction cookie from this transaction.
	 */
	public WiserCookie getTransactionCookie()
	{
		return this.wiserCookie;
	}

	/**
	 * @return the receiveContent may be empty if there was none.
	 */
	public String getContent()
	{
		return receiveContent;
	}

	/**
	 * @param receiveContent the receiveContent to set
	 */
	public void setContent( String content )
	{
		this.receiveContent = content;
	}

}
