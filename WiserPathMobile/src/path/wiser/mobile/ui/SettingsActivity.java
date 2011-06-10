/**
 * 
 */
package path.wiser.mobile.ui;

import path.wiser.mobile.R;
import path.wiser.mobile.WiserPathMobile;
import path.wiser.mobile.services.Credential;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
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
		userNamePref.setOnPreferenceChangeListener( new OnPreferenceChangeListener()
		{

			@Override
			public boolean onPreferenceChange( Preference preference, Object prefValue ) // what is arg1
			{
				SharedPreferences customSharedPreference = getSharedPreferences( SettingsActivity.PREFS_FILE_NAME, Activity.MODE_PRIVATE );
				SharedPreferences.Editor editor = customSharedPreference.edit();
				editor.putString( Credential.USER_NAME, prefValue.toString() );
				editor.commit();
				Toast.makeText( getBaseContext(), "User name updated", Toast.LENGTH_LONG ).show();
				return true;
			}
		} );

		// add a listener so when the user changes their password it is noted and changed in the preference file.
		EditTextPreference passwordPref = (EditTextPreference) findPreference( Credential.PASSWORD );
		passwordPref.setOnPreferenceChangeListener( new OnPreferenceChangeListener()
		{

			@Override
			public boolean onPreferenceChange( Preference preference, Object prefValue )
			{
				SharedPreferences customSharedPreference = getSharedPreferences( SettingsActivity.PREFS_FILE_NAME, Activity.MODE_PRIVATE );
				SharedPreferences.Editor editor = customSharedPreference.edit();
				editor.putString( Credential.PASSWORD, prefValue.toString() );
				editor.commit();
				Toast.makeText( getBaseContext(), "Password updated", Toast.LENGTH_LONG ).show();
				return true;
			}
		} );

		// create a new click listener for the switch. It is used later to save the setting.
		this.useLocation = new WiserPreferenceClickListener( WiserPathMobile.Tab.POI.ordinal() );
		Preference useLocationPreference = (Preference) findPreference( SettingsActivity.USE_LOCATION );
		useLocationPreference.setOnPreferenceClickListener( this.useLocation );
		this.useLocation.set( useLocationPreference.isEnabled() );

		// Check if user has access to WIFI?
		// Does user have access through phone?
		// does user want access via either?
		// does their user name and password authenticate?

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
	 * Registers a click occurred and saves the new value to the preferences.
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