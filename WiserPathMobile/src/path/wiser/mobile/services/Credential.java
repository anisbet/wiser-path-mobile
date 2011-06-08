/**
 * 
 */
package path.wiser.mobile.services;


/**
 * This is the user's name and password plus the business rules for how that is
 * stored retrieved
 * and its validity tested.
 * 
 * @author andrewnisbet
 * 
 */
public class Credential
{
	public static final String	USER_NAME			= "USER_NAME";			// if this change they
																			// must
																			// also
																			// be
																			// changed
																			// in
																			// the
																			// res/xml/preferences.xml
																			// file
	public static final String	PASSWORD			= "PASSWORD";			// if
																			// this
																			// change
																			// they
																			// must
																			// also
																			// be
																			// changed
																			// in
																			// the
																			// res/xml/preferences.xml
																			// file
	public static final String	DEFAULT_USER_NAME	= "user";				// TODO
																			// WARNING:
																			// change
																			// this
																			// for
																			// production
	public static final String	DEFAULT_PASSWORD	= "password";			// TODO
																			// Change
																			// this.
	private String				userName			= null;
	private String				password			= null;
	private Status				userStatus			= Status.NON_MEMBER;
	private WiserCookie			loginCookie			= null;

	public enum Status
	{
		NON_MEMBER, MEMBER
	}

	/**
	 * This constructor is used to create an intial credential which HTTPService
	 * will use
	 * to authenticate you to the web site. After that if you are a valid user
	 * it will
	 * give this object an authentication cookie which will be used for further
	 * transactions.
	 * 
	 * @param name
	 * @param password
	 */
	public Credential( String name, String password )
	{
		this.userName = name;
		this.password = password;
		// this person is not a member until they get a cookie.
	}

	/**
	 * @return the userName
	 */
	public final String getUserName()
	{
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public final void setUserName( String userName )
	{
		this.userName = userName;
	}

	/**
	 * @return the password
	 */
	public final String getPassword()
	{
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public final void setPassword( String password )
	{
		this.password = password;
	}

	public boolean isMember()
	{
		return this.userStatus == Status.MEMBER;
	}

	/**
	 * @param wiserCookie
	 */
	public void setCookie( WiserCookie wiserCookie )
	{
		this.loginCookie = wiserCookie;
		if (this.loginCookie != null)
		{
			this.userStatus = Status.MEMBER;
		}
	}

	/**
	 * @return the loginCookie
	 */
	public WiserCookie getLoginCookie()
	{
		return loginCookie;
	}
}
