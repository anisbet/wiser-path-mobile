/**
 * 
 */
package path.wiser.mobile.util;

/**
 * @author andrew nisbet
 * 
 *         This class tests if the user is a valid Wiser Path user
 *         and manages the user login and authentication with the web site.
 * 
 * 
 */
public class LoginManager
{

	public final static String	LOGIN_PATH	= "";	// "/user/login";
	private HttpManager			httpManager	= null;

	public LoginManager()
	{
		httpManager = new HttpManager( LOGIN_PATH );
		httpManager.postLogin( "anisbet", "password" );
	}

}
