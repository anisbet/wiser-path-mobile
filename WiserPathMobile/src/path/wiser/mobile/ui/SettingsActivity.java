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
import android.preference.PreferenceActivity;
import android.widget.Toast;

/**
 * @author andrew nisbet
 * 
 */
public class SettingsActivity extends PreferenceActivity
{
	private static final String	USE_LOCATION	= "USE_LOCATION";
	private static final String	WORK_ONLINE		= "WORK_ONLINE";
	private boolean				useLocation;
	private boolean				workOnline;

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
				SharedPreferences customSharedPreference = getSharedPreferences( WiserPathMobile.PREFS_FILE_NAME, Activity.MODE_PRIVATE );
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
				SharedPreferences customSharedPreference = getSharedPreferences( WiserPathMobile.PREFS_FILE_NAME, Activity.MODE_PRIVATE );
				SharedPreferences.Editor editor = customSharedPreference.edit();
				editor.putString( Credential.PASSWORD, prefValue.toString() );
				editor.commit();
				Toast.makeText( getBaseContext(), "Password updated", Toast.LENGTH_LONG ).show();
				return true;
			}
		} );

		// Get the custom preference
		SharedPreferences mySharedPreferences = getSharedPreferences( WiserPathMobile.PREFS_FILE_NAME, Activity.MODE_PRIVATE );
		this.useLocation = mySharedPreferences.getBoolean( SettingsActivity.USE_LOCATION, true );
		this.workOnline = mySharedPreferences.getBoolean( SettingsActivity.WORK_ONLINE, true );

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
		SharedPreferences mySharedPreferences = getSharedPreferences( WiserPathMobile.PREFS_FILE_NAME, Activity.MODE_PRIVATE );
		this.useLocation = mySharedPreferences.getBoolean( SettingsActivity.USE_LOCATION, true );
		this.workOnline = mySharedPreferences.getBoolean( SettingsActivity.WORK_ONLINE, true );
		// the user name and password are already done because they have change listeners that will commit any changes.
		SharedPreferences.Editor editor = mySharedPreferences.edit();
		editor.putBoolean( SettingsActivity.USE_LOCATION, this.useLocation );
		editor.putBoolean( SettingsActivity.WORK_ONLINE, this.workOnline );
		editor.commit();
	}

}