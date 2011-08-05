/**
 * 
 */
package path.wiser.mobile;

import path.wiser.mobile.services.Credential;
import path.wiser.mobile.services.HTTPService;
import path.wiser.mobile.ui.IncidentActivity;
import path.wiser.mobile.ui.PointOfInterestActivity;
import path.wiser.mobile.ui.SettingsActivity;
import path.wiser.mobile.ui.TraceActivity;
import path.wiser.mobile.ui.WiserPathActivity;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabWidget;

/**
 * @author andrew nisbet
 * 
 */
public class WiserPathMobile extends TabActivity
{
	public static enum Tab
	{
		TRACE, POI, WP, INCIDENT, PREFS
	}

	private SharedPreferences	sharedPreferences	= null;
	private static TabHost		tabHost				= null;

	public void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		setContentView( R.layout.main );

		Resources res = getResources(); // Resource object to get Drawables
		// TabHost tabHost = getTabHost(); // The activity TabHost
		tabHost = getTabHost(); // The activity TabHost
		TabHost.TabSpec spec; // Reusable TabSpec for each tab

		Intent intent; // Reusable Intent for each tab

		// Create an Intent to launch an Activity for the tab (to be reused)
		intent = new Intent().setClass( this, TraceActivity.class );
		// Initialize a TabSpec for each tab and add it to the TabHost
		spec = tabHost.newTabSpec( "Trace" ).setIndicator( "Trace", res.getDrawable( R.drawable.ic_tab_trace ) ).setContent( intent );
		tabHost.addTab( spec );
		// TODO internationalize the names of the tabs
		// Do the same for the other tabs
		intent = new Intent().setClass( this, PointOfInterestActivity.class );
		// Initialize a TabSpec for each tab and add it to the TabHost
		spec = tabHost.newTabSpec( "POI" ).setIndicator( "POI", res.getDrawable( R.drawable.ic_tab_poi ) ).setContent( intent );
		tabHost.addTab( spec );

		intent = new Intent().setClass( this, WiserPathActivity.class );
		spec = tabHost.newTabSpec( "Wiser Path" ).setIndicator( "Wiser Path", res.getDrawable( R.drawable.ic_tab_witp ) ).setContent( intent );
		tabHost.addTab( spec );

		intent = new Intent().setClass( this, IncidentActivity.class );
		spec = tabHost.newTabSpec( "Incident" ).setIndicator( "Incident", res.getDrawable( R.drawable.ic_tab_incident ) ).setContent( intent );
		tabHost.addTab( spec );

		intent = new Intent().setClass( this, SettingsActivity.class );
		spec = tabHost.newTabSpec( "Options" ).setIndicator( "Options", res.getDrawable( R.drawable.ic_tab_options ) ).setContent( intent );
		tabHost.addTab( spec );

		// Try to login.
		// Determine which tab should be shown first -- we will also have to account for new users registering.
		// TODO set tabs
		if (login() == false)
		{
			// tabHost.setCurrentTab( WiserPathMobile.PREFS_TAB );
			tabHost.setCurrentTab( Tab.PREFS.ordinal() );
		}
		else
		{
			tabHost.setCurrentTab( Tab.WP.ordinal() );
		}

	}

	/**
	 * @return Credential object with either the username password from
	 *         preferences or the default values from Credential.
	 */
	private boolean login()
	{
		sharedPreferences = this.getSharedPreferences( SettingsActivity.PREFS_FILE_NAME, Activity.MODE_PRIVATE );
		// the user name will be set to what ever is in the private preference file and if there isn't one
		// will be set to the default value (found in Credential.java).
		String userName = sharedPreferences.getString( Credential.USER_NAME, Credential.DEFAULT_USER_NAME );
		String password = sharedPreferences.getString( Credential.PASSWORD, Credential.DEFAULT_PASSWORD );

		// TODO keep this for saving POI::Bitmap bm =
		// BitmapFactory.decodeFile( "/data/misc/wallpaper/" + bmpName
		// );
		// TODO delete this for production version.
		// Toast.makeText( this, "User name and password have been read from preferences:" + "userName=" + userName +
		// " password:" + password,
		// Toast.LENGTH_LONG ).show();
		// Now try and make a connection and login.
		HTTPService httpService = HTTPService.login( userName, password );
		try
		{
			return httpService.isLoggedIn();
		}
		catch (NullPointerException e)
		{
			return false;
		}
	}

	/**
	 * Turns a tab either on or off.
	 */
	public static final void enableTab( Tab tabNumber, boolean onOff )
	{
		TabWidget tabWidget = tabHost.getTabWidget();
		View tab = tabWidget.getChildTabViewAt( tabNumber.ordinal() );
		tab.setEnabled( onOff );
	}

}
