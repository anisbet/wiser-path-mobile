/**
 * 
 */
package path.wiser.mobile.test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import junit.framework.TestCase;

import org.junit.Test;

import path.wiser.mobile.services.WiserResponse;

/**
 * @author andrewnisbet
 *
 */
public class WiserResponseTest extends TestCase {

	/**
	 * Test method for {@link path.wiser.mobile.services.WiserResponse#WiserResponse(java.net.URLConnection, java.lang.String)}.
	 */
	@Test
	public void testWiserResponse() {
		
		String dataToPost = "name=" + "anisbet" + "&pass=" + "leader1" + "&form_id=user_login";
		URL url;
		try {
			url = new URL( "http://wiserpath-dev.bus.ualberta.ca/user/login" );
			URLConnection connection = url.openConnection();
			WiserResponse response = new WiserResponse( connection, URLEncoder.encode( dataToPost, "UTF-8" ) );
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
