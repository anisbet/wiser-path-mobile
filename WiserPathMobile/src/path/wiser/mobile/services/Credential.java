/**
 * 
 */
package path.wiser.mobile.services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;

import android.util.Log;

/**
 * This is the user's name and password plus the business rules for how that is
 * stored retrieved
 * and its validity tested.
 * 
 * @author andrewnisbet
 * 
 */
public class Credential implements Serializable
{
	private static final long	serialVersionUID	= -3248215391323111435L;
	private String				userName			= null;
	private String				password			= null;
	private Status				userStatus			= Status.NON_MEMBER;
	private int					MIN_LENGTH			= 8;
	private final static String	keyRing				= null;

	public enum Status
	{
		NON_MEMBER, MEMBER
	}

	public Credential()
	{
		// retreive the username and password stored on the device.
		getUserNamePassword();
	}

	/**
	 * @return true if the user name and password could be serialized to file
	 *         and false otherwise.
	 */
	public boolean serialize()
	{
		if (this.userName != null && this.userName.length() > 0 && this.password != null && this.password.length() > MIN_LENGTH)
		{
			File keys = new File( keyRing );
			BufferedWriter keyFile;
			try
			{
				keyFile = new BufferedWriter( new FileWriter( keys ) );
				keyFile.write( userName );
				keyFile.write( password );
				keyFile.close();
			}
			catch (IOException e)
			{
				return false;
			}
			return true;
		}
		else
		{
			Log.w( "Credential:error", "either the user name was empty or the password was not " + MIN_LENGTH + " characters long." );
		}
		return false;

	}

	/**
	 * Populates the user name and password from file.
	 */
	private void getUserNamePassword()
	{
		// test if user name password file exists and if so read it
		File keys = new File( keyRing );
		if (keys.exists() == false)
		{
			userStatus = Status.NON_MEMBER;
			return;
		}
		// if not, read the file and see if they are valid.
		BufferedReader keyFile = null;
		try
		{
			keyFile = new BufferedReader( new FileReader( keys ) );
			this.userName = keyFile.readLine();
			this.password = keyFile.readLine();
			keyFile.close();
			this.userStatus = Status.MEMBER;
		}
		catch (FileNotFoundException e)
		{
			Log.w( "Credential:error", "the keyring file could not be found." );
		}
		catch (IOException e)
		{
			Log.w( "Credential:error", "unable to read keyring file." );
		}

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

	/**
	 * @param userStatus the userStatus to set
	 */
	public final void setUserStatus( Status userStatus )
	{
		this.userStatus = userStatus;
	}

	public boolean isMember()
	{
		return this.userStatus == Status.MEMBER;
	}
}
