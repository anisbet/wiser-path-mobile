/**
 * 
 */
package path.wiser.mobile.services;

import java.net.URL;

/**
 * This class manages the communication of the andoid device and WiserPath
 * services.
 * 
 * @author andrewnisbet
 * 
 */
public class HTTPService
{
	private final static String	WISERPATH_HOST		= "http://about.wiserpath-dev.bus.ualberta.ca";
	private URL					wiserURL			= null;
	private Credential			usersCredentials	= null;

	public HTTPService( Credential loginCredential )
	{
		this.usersCredentials = loginCredential;
		if (this.usersCredentials.isMember() == false)
		{
			// send him to sign up.
		}
		else
		{
			// log him in.
		}
	}

}
