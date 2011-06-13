/**
 * 
 */
package path.wiser.mobile.ui;

import path.wiser.mobile.R;
import path.wiser.mobile.WiserPathMobile;
import path.wiser.mobile.services.Credential;
import path.wiser.mobile.services.HTTPService;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.widget.Toast;

/**
 * @author andrew nisbet
 * 
 */
public class SettingsActivity extends PreferenceActivity
{
	public static final String				PREFS_FILE_NAME		= "MyPrefsFile";
	public static final String				USE_LOCATION		= "USE_LOCATION";
	// USER_NAME contained in Credential.
	public static final String				REGISTER_USER_NAME	= "REGISTER_USER_NAME";
	public static final String				EMAIL				= "EMAIL";
	private WiserPreferenceClickListener	useLocation;

	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		addPreferencesFromResource( R.xml.preferences );
		// add change listeners to all the preference UI objects to see if they change and update the system
		// appropriately.
		Preference userNamePref = (Preference) findPreference( Credential.USER_NAME );
		WiserLoginChangeListener wlcl = new WiserLoginChangeListener();
		userNamePref.setOnPreferenceChangeListener( wlcl );

		// add a listener so when the user changes their password it is noted and changed in the preference file.
		// the listener remembers user name and password, and will check again if both user name and password have
		// had a change or have some value.
		Preference passwordPref = (Preference) findPreference( Credential.PASSWORD );
		passwordPref.setOnPreferenceChangeListener( wlcl );

		Preference regUserName = (Preference) findPreference( SettingsActivity.REGISTER_USER_NAME );
		regUserName.setOnPreferenceChangeListener( wlcl );

		Preference regEmail = (Preference) findPreference( SettingsActivity.EMAIL );
		regEmail.setOnPreferenceChangeListener( wlcl );

		// create a new click listener for the switch. It is used later to save the setting.
		this.useLocation = new WiserPreferenceClickListener( WiserPathMobile.Tab.POI.ordinal() );
		Preference useLocationPreference = (Preference) findPreference( SettingsActivity.USE_LOCATION );
		useLocationPreference.setOnPreferenceClickListener( this.useLocation );
		this.useLocation.set( useLocationPreference.isEnabled() );

	}

	@Override
	protected void onPause()
	{
		super.onPause();
		commitChanges();
	}

	private void commitChanges()
	{
		SharedPreferences mySharedPreferences = getSharedPreferences( SettingsActivity.PREFS_FILE_NAME, Activity.MODE_PRIVATE );
		// the user name and password are already done because they have change listeners that will commit any changes.
		SharedPreferences.Editor editor = mySharedPreferences.edit();
		editor.putBoolean( SettingsActivity.USE_LOCATION, useLocation.getCurrentSetting() );
		editor.commit();
	}

	/**
	 * Used to listen for login and password changes but also to attempt to re-login if the user changes user name and
	 * or password.
	 * 
	 * @author andrewnisbet
	 * 
	 */
	public class WiserLoginChangeListener implements OnPreferenceChangeListener
	{
		private String	name		= null;
		private String	password	= null;

		@Override
		public boolean onPreferenceChange( Preference preference, Object prefValue )
		{

			if (preference.getKey().equals( Credential.USER_NAME ) || preference.getKey().equals( SettingsActivity.REGISTER_USER_NAME ))
			{
				name = prefValue.toString();
			}
			else
				if (preference.getKey().equals( Credential.PASSWORD ))
				{
					password = prefValue.toString();
				}
				else
					if (preference.getKey().equals( SettingsActivity.EMAIL ))
					{
						return register( prefValue.toString() );
					}

			return loginAgain();
		}

		/**
		 * @param email
		 * @return True if the person could register and false otherwise.
		 */
		private boolean register( String email )
		{
			if (name != null)
			{
				if (HTTPService.signUp( name, email ))
				{
					SharedPreferences customSharedPreference = getSharedPreferences( SettingsActivity.PREFS_FILE_NAME, Activity.MODE_PRIVATE );
					SharedPreferences.Editor editor = customSharedPreference.edit();
					editor.putString( Credential.USER_NAME, name );
					editor.commit();
					Toast.makeText( getBaseContext(), "Welcome to Wiser Path! Check your email for further instructions.", Toast.LENGTH_LONG ).show();
					return true;
				}
			}
			else
			{
				Toast.makeText( getBaseContext(), "Uh-oh, there was a problem registering you. Please visit WiserPath for help.", Toast.LENGTH_LONG )
					.show();
			}
			return false;
		}

		/**
		 * @return true if the user could be logged in and false otherwise.
		 */
		private boolean loginAgain()
		{
			if (name != null && password != null)
			{
				HTTPService service = HTTPService.login( name, password );
				if (service.isLoggedIn() == true) // save these values
				{
					SharedPreferences customSharedPreference = getSharedPreferences( SettingsActivity.PREFS_FILE_NAME, Activity.MODE_PRIVATE );
					SharedPreferences.Editor editor = customSharedPreference.edit();
					editor.putString( Credential.USER_NAME, name );
					editor.putString( Credential.PASSWORD, password );
					editor.commit();
					Toast.makeText( getBaseContext(), "You are now logged in as " + name, Toast.LENGTH_LONG ).show();
					return true;
				}
				Toast.makeText( getBaseContext(), "Uh-oh, logging in as '" + name + "' failed; are your name and password correct?",
					Toast.LENGTH_LONG ).show();
				return false;
			}
			return false;
		}

	}

	/**
	 * Registers a click occurred and saves the new value to the preferences.
	 * This class also encapsulates the boolean value as well.
	 * 
	 * @author andrewnisbet
	 * 
	 */
	public class WiserPreferenceClickListener implements OnPreferenceClickListener
	{
		private int		modifyTabNumber;
		private boolean	setting	= true;

		public WiserPreferenceClickListener( int whichTabModified )
		{
			this.modifyTabNumber = whichTabModified;
		}

		public WiserPreferenceClickListener()
		{
			this.modifyTabNumber = -1;
		}

		@Override
		public boolean onPreferenceClick( Preference preference )
		{
			this.setting = !this.setting;
			if (this.modifyTabNumber >= 0)
			{
				// Toast.makeText( getBaseContext(), "The setting is " + setting + "Setting tab..." + modifyTabNumber,
				// Toast.LENGTH_LONG ).show();
				WiserPathMobile.enableTab( modifyTabNumber, this.setting );
			}
			// write out the new setting back to preferences.
			// Toast.makeText( getBaseContext(), "Setting changed", Toast.LENGTH_LONG ).show();
			return true;
		}

		/**
		 * @return
		 */
		public boolean getCurrentSetting()
		{
			return this.setting;
		}

		/**
		 * @param true if on and false otherwise.
		 */
		public void set( boolean enabled )
		{
			this.setting = enabled;
		}

	}
}