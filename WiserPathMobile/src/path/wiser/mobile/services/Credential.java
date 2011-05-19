/**
 * 
 */
package path.wiser.mobile.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

/**
 * This is the user's name and password plus the business rules for how that is
 * stored retrieved
 * and its validity tested.
 * 
 * @author andrewnisbet
 * 
 */
public class Credential extends Activity implements Serializable
{
	private static final long	serialVersionUID	= -3248215391323111435L;
	private String				userName			= null;
	private String				password			= null;
	private Status				userStatus			= Status.NON_MEMBER;
	private int					MIN_LENGTH			= 4;
	public final static String	keyRing				= "res/raw/keyring.wpm";
	private static final int	BUFFER_SIZE			= 64;
	private byte[]				buffer				= new byte[BUFFER_SIZE + 1];

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
			// Create a new file in res/raw
			FileOutputStream fos = null;
			File file = new File( keyRing );
			try
			{
				file.createNewFile();
				fos = openFileOutput( keyRing, Context.MODE_PRIVATE );
				this.buffer = this.userName.getBytes();
				fos.write( this.buffer );
				this.buffer = this.password.getBytes();
				fos.write( buffer );
				fos.close();
			}
			catch (FileNotFoundException e)
			{
				Log.w( "Credential:serialize", "The keyring file could not be found." );
				return false;
			}
			catch (IOException e)
			{
				Log.w( "Credential:serialize", "Error writing to file." );
				return false;
			}
			return true;
		}
		else
		{
			Log.w( "Credential:serialize", "either the user name was empty or the password was not " + MIN_LENGTH + " characters long." );
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
		FileInputStream keyFile = null;

		try
		{
			int bytesRead = 0;
			keyFile = openFileInput( keyRing );
			bytesRead = keyFile.read( this.buffer );
			if (bytesRead == BUFFER_SIZE)
			{
				this.userName = new String( this.buffer );
				Log.w( ">>>", this.userName ); // for testing.
				this.buffer = new byte[BUFFER_SIZE + 1]; // clear the buffer.
				bytesRead = keyFile.read( this.buffer );
				this.password = new String( this.buffer );
				Log.w( ">>>", this.password ); // for testing.
				this.userStatus = Status.MEMBER;
			}
			keyFile.close();
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
