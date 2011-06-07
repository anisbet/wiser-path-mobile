/**
 * 
 */
package path.wiser.mobile.ui;

import path.wiser.mobile.R;
import path.wiser.mobile.WiserPathMobile;
import path.wiser.mobile.services.Credential;
import path.wiser.mobile.util.LoginManager;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author andrew nisbet
 * 
 */
public class SettingsActivity extends PreferenceActivity
{
	private LoginManager	loginManager;

	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		addPreferencesFromResource( R.xml.preferences );
		// add change listeners to all the preference UI objects to see if they change and update the system
		// appropriately.
		EditTextPreference userNamePref = (EditTextPreference) findPreference( Credential.USER_NAME );
		userNamePref.setOnPreferenceChangeListener( new OnPreferenceChangeListener()
		{

			@Override
			public boolean onPreferenceChange( Preference preference, Object prefValue ) // what is arg1
			{
				SharedPreferences customSharedPreference = getSharedPreferences( WiserPathMobile.PREFS_FILE_NAME, Activity.MODE_PRIVATE );
				SharedPreferences.Editor editor = customSharedPreference.edit();
				editor.putString( Credential.USER_NAME, prefValue.toString() );
				editor.commit();
				Toast.makeText( getBaseContext(), "The preference user name has changed: " + prefValue.toString(), Toast.LENGTH_LONG ).show();
				return true;
			}
		} );

		EditTextPreference passwordPref = (EditTextPreference) findPreference( Credential.PASSWORD );
		passwordPref.setOnPreferenceChangeListener( new OnPreferenceChangeListener()
		{

			@Override
			public boolean onPreferenceChange( Preference preference, Object prefValue ) // what is arg1
			{
				SharedPreferences customSharedPreference = getSharedPreferences( WiserPathMobile.PREFS_FILE_NAME, Activity.MODE_PRIVATE );
				SharedPreferences.Editor editor = customSharedPreference.edit();
				editor.putString( Credential.PASSWORD, prefValue.toString() );
				editor.commit();
				Toast.makeText( getBaseContext(), "The preference password has changed", Toast.LENGTH_LONG ).show();
				return true;
			}
		} );

		// loginManager = new LoginManager();
		// System.out.println( loginManager.toString() );
		// Check if user has access to WIFI?
		// Does user have access through phone?
		// does user want access via either?
		// does their user name and password authenticate?

	}

	/**
	 * This method creates a Toast with the supplied message.
	 * 
	 * @param message
	 */
	protected void showMessage( String message )
	{
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate( R.layout.custom_toast_layout, (ViewGroup) findViewById( R.id.custom_toast_layout_root ) );
		ImageView image = (ImageView) layout.findViewById( R.id.image );
		image.setImageResource( R.drawable.icon );
		TextView text = (TextView) layout.findViewById( R.id.text );
		text.setText( message );

		Toast toast = new Toast( getApplicationContext() );
		toast.setGravity( Gravity.CENTER_VERTICAL, 0, 0 );
		toast.setDuration( Toast.LENGTH_LONG );
		toast.setView( layout );
		toast.show();

	}

	@Override
	protected void onStop()
	{
		super.onStop();
	}
}