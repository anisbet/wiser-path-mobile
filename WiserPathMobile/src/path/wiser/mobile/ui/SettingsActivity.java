/**
 * 
 */
package path.wiser.mobile.ui;

import path.wiser.mobile.ActivityType;
import path.wiser.mobile.R;
import path.wiser.mobile.Units;
import path.wiser.mobile.WPEnvironment;
import path.wiser.mobile.WiserPathMobile;
import path.wiser.mobile.services.Credential;
import path.wiser.mobile.services.HTTPService;
import path.wiser.mobile.util.MediaIO;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.text.Html;
import android.widget.Toast;

/**
 * @author andrew nisbet
 * 
 */
public class SettingsActivity extends PreferenceActivity
{
	public static final String				PREFS_FILE_NAME			= "MyPrefsFile";
	public static final String				USE_LOCATION			= "USE_LOCATION";			// These are keys from
																								// the
																								// xml/preferences.xml
																								// file.
	public static final String				USE_EXTERNAL_STORAGE	= "USE_EXTERNAL_STORAGE";
	// USER_NAME contained in Credential.
	public static final String				REGISTER_USER_NAME		= "REGISTER_USER_NAME";
	public static final String				EMAIL					= "EMAIL";
	private WiserPreferenceClickListener	useLocationClickListener;
	private WiserPreferenceClickListener	useSDCardClickListener;

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

		// create a register user name preference object
		Preference regUserName = (Preference) findPreference( SettingsActivity.REGISTER_USER_NAME );
		regUserName.setOnPreferenceChangeListener( wlcl );

		// create a register user email object.
		Preference regEmail = (Preference) findPreference( SettingsActivity.EMAIL );
		regEmail.setOnPreferenceChangeListener( wlcl );

		// create a new click listener for the switch. It is used later to save the setting.
		this.useLocationClickListener = new UseLocationClickListener();
		Preference useLocationPreference = (Preference) findPreference( SettingsActivity.USE_LOCATION );
		useLocationPreference.setOnPreferenceClickListener( this.useLocationClickListener );
		this.useLocationClickListener.set( useLocationPreference.isEnabled() );

		// create a new click listener external memory use switch.
		this.useSDCardClickListener = new UseExternalStorageClickListener();
		Preference useSDCardPreference = (Preference) findPreference( SettingsActivity.USE_EXTERNAL_STORAGE );
		if (MediaIO.deviceHasWritableExternalMedia()) // this adds a listener only if there is a card and it's writable
		{
			useSDCardPreference.setOnPreferenceClickListener( this.useSDCardClickListener );
			this.useSDCardClickListener.set( useSDCardPreference.isEnabled() );
		}
		useSDCardPreference.setEnabled( MediaIO.deviceHasWritableExternalMedia() ); // if true enable and disable
																					// otherwise.
		// TODO figure out how to get the preference for activity type. for now just set it to Walk
		Units.setActivityType( ActivityType.WALK );
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
		editor.putBoolean( SettingsActivity.USE_LOCATION, useLocationClickListener.getCurrentSetting() );
		editor.putBoolean( SettingsActivity.USE_EXTERNAL_STORAGE, useSDCardClickListener.getCurrentSetting() );
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
			Resources res = getResources();
			String text = "";
			CharSequence msg = "";
			if (name != null)
			{
				if (HTTPService.isValidUserName( name ) == false)
				{
					text = String.format( res.getString( R.string.s_invalid_user_name_msg ) );
					return false;
				}
				if (HTTPService.isValidEmailAddress( email ) == false)
				{
					text = String.format( res.getString( R.string.s_invalid_email_msg ) );
					return false;
				}
				if (HTTPService.signUp( name, email ))
				{
					// save the settings
					SharedPreferences customSharedPreference = getSharedPreferences( SettingsActivity.PREFS_FILE_NAME, Activity.MODE_PRIVATE );
					SharedPreferences.Editor editor = customSharedPreference.edit();
					editor.putString( Credential.USER_NAME, name );
					editor.commit();
					// now give the user some feed back.
					text = String.format( res.getString( R.string.s_welcome_msg ), this.name );
					return true;
				}
			}
			else
			{
				text = String.format( res.getString( R.string.s_unwelcome_msg ) );
			}
			msg = Html.fromHtml( text );
			Toast.makeText( getBaseContext(), msg, Toast.LENGTH_LONG ).show();
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
				Resources res = getResources();
				if (service.isLoggedIn() == true) // save these values
				{
					// save the settings
					SharedPreferences customSharedPreference = getSharedPreferences( SettingsActivity.PREFS_FILE_NAME, Activity.MODE_PRIVATE );
					SharedPreferences.Editor editor = customSharedPreference.edit();
					editor.putString( Credential.USER_NAME, name );
					editor.putString( Credential.PASSWORD, password );
					editor.commit();
					// use localized message string to show success or failure in desired language.
					// String escapedUsername = TextUtil.htmlEncode( this.name );
					String text = String.format( res.getString( R.string.s_login_success_msg ), this.name );
					CharSequence msg = Html.fromHtml( text );
					Toast.makeText( getBaseContext(), msg, Toast.LENGTH_LONG ).show();
					return true;
				}
				String text = String.format( res.getString( R.string.s_login_fail_msg ), this.name );
				CharSequence msg = Html.fromHtml( text );
				Toast.makeText( getBaseContext(), msg, Toast.LENGTH_LONG ).show();
			}
			return false;
		}

	}

	/**
	 * Registers a click occurred and saves the new value to the preferences.
	 * This class also encapsulates the boolean value as well, but extend it
	 * to do something useful like alerting a class of a change.
	 * 
	 * @see UseLocationClickListener, UseExternalStorageClickListener
	 * 
	 * @author andrewnisbet
	 * 
	 */
	public class WiserPreferenceClickListener implements OnPreferenceClickListener
	{
		protected boolean	setting;

		@Override
		public boolean onPreferenceClick( Preference preference )
		{
			this.setting = !this.setting;
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

	/**
	 * Registers a click occurred and saves the new value to the preferences.
	 * This class also encapsulates the boolean value as well.
	 * 
	 * @author andrewnisbet
	 * 
	 */
	public class UseLocationClickListener extends WiserPreferenceClickListener
	{

		@Override
		public boolean onPreferenceClick( Preference preference )
		{
			this.setting = !this.setting;
			WiserPathMobile.enableTab( WiserPathMobile.Tab.INCIDENT, this.setting );
			WiserPathMobile.enableTab( WiserPathMobile.Tab.TRACE, this.setting );
			WiserPathMobile.enableTab( WiserPathMobile.Tab.POI, this.setting );
			return true;
		}
	}

	/**
	 * Alerts the environment about the preference of the user as far as using external storage preference.
	 * 
	 * @author andrewnisbet
	 * 
	 */
	public class UseExternalStorageClickListener extends WiserPreferenceClickListener
	{
		@Override
		public boolean onPreferenceClick( Preference preference )
		{
			this.setting = !this.setting;
			WPEnvironment.setPreferExternalStorage( this.setting );
			return true;
		}
	}
}