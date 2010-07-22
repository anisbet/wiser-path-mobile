/**
 * 
 */
package path.wiser.mobile;

import path.wiser.mobile.ui.IncidentActivity;
import path.wiser.mobile.ui.PointOfInterestActivity;
import path.wiser.mobile.ui.SettingsActivity;
import path.wiser.mobile.ui.TraceActivity;
import path.wiser.mobile.ui.WiserPathActivity;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

/**
 * @author andrew nisbet
 * 
 */
public class WiserPathMobile extends TabActivity
{

	public void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		setContentView( R.layout.main );

		Resources res = getResources(); // Resource object to get Drawables
		TabHost tabHost = getTabHost(); // The activity TabHost
		TabHost.TabSpec spec; // Resusable TabSpec for each tab
		Intent intent; // Reusable Intent for each tab

		// Create an Intent to launch an Activity for the tab (to be reused)
		intent = new Intent().setClass( this, TraceActivity.class );
		// Initialize a TabSpec for each tab and add it to the TabHost
		spec = tabHost.newTabSpec( "Trace" )
			.setIndicator( "Trace", res.getDrawable( R.drawable.ic_tab_trace ) )
			.setContent( intent );
		tabHost.addTab( spec );

		// Do the same for the other tabs
		intent = new Intent().setClass( this, PointOfInterestActivity.class );
		// Initialize a TabSpec for each tab and add it to the TabHost
		spec = tabHost.newTabSpec( "POI" )
			.setIndicator( "POI", res.getDrawable( R.drawable.ic_tab_poi ) )
			.setContent( intent );
		tabHost.addTab( spec );

		intent = new Intent().setClass( this, WiserPathActivity.class );
		spec = tabHost
			.newTabSpec( "Wiser Path" )
			.setIndicator( "Wiser Path",
				res.getDrawable( R.drawable.ic_tab_witp ) ).setContent( intent );
		tabHost.addTab( spec );

		intent = new Intent().setClass( this, IncidentActivity.class );
		spec = tabHost
			.newTabSpec( "Incident" )
			.setIndicator( "Incident",
				res.getDrawable( R.drawable.ic_tab_incident ) )
			.setContent( intent );
		tabHost.addTab( spec );

		intent = new Intent().setClass( this, SettingsActivity.class );
		spec = tabHost
			.newTabSpec( "Options" )
			.setIndicator( "Options",
				res.getDrawable( R.drawable.ic_tab_options ) )
			.setContent( intent );
		tabHost.addTab( spec );

		tabHost.setCurrentTab( 2 );
	}
}
