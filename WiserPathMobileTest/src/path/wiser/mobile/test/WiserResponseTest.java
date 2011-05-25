/**
 * 
 */
package path.wiser.mobile.test;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import junit.framework.TestCase;

import org.junit.Test;

import path.wiser.mobile.services.Post;

/**
 * @author andrewnisbet
 *
 */
public class WiserResponseTest extends TestCase {

	@Test
	public void testNullPost() {
		String dataToPost = "name=" + "anisbet" + "&pass=" + "leader1" + "&form_id=user_login";
		URL url = null;
		System.out.print( "An error message here is normal: " );
		try {
			//url = new URL( "http://wiserpath-dev.bus.ualberta.ca/user/login" );
			dataToPost = URLEncoder.encode( dataToPost, "UTF-8" );
			Post response = new Post( url );
			response.post( dataToPost );
			System.out.println( "the return code was: '" + response.getReturnCode() + "'" );
		} catch (Exception e) {
			assertTrue( e instanceof NullPointerException );
		}
	}
	
	@Test
	public void testInvalidURL() {
		String dataToPost = "name=" + "anisbet" + "&pass=" + "leader1" + "&form_id=user_login";
		URL url = null;
		Post response = null;
		System.out.print( "An error message here is normal: " );
		try {
			url = new URL( "http://www/user/login" );
			dataToPost = URLEncoder.encode( dataToPost, "UTF-8" );
			response = new Post( url );
			response.post( dataToPost );
			System.out.println( "the return code was: '" + response.getReturnCode() + "'" );
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertTrue( response.getReturnCode() == 400 );
	}
	
	@Test
	public void testValidPost() {
		String dataToPost = "name=" + "anisbet" + "&pass=" + "leader1" + "&form_id=user_login&op=log+in";
		Post response = null;
		URL url = null;
		try {
			url = new URL( "http://wiserpath-dev.bus.ualberta.ca/user/login" );
			//dataToPost = URLEncoder.encode( dataToPost, "UTF-8" );
			response = new Post(url);
			System.out.println( "OUTPUT:" + response.post(dataToPost) );
			
//			System.out.println( "the return code was: '" + response.getReturnCode() + "'" );
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertTrue( response.getReturnCode() == 200 );
		System.out.println( ">>>" + response.getReturnCode() );
	}

}
