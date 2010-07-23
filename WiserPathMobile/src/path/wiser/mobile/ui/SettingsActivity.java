/**
 * 
 */
package path.wiser.mobile.ui;

import path.wiser.mobile.R;
import path.wiser.mobile.util.HttpManager;
import path.wiser.mobile.util.LoginManager;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;
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
	LoginManager	loginManager;

	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		addPreferencesFromResource( R.xml.preferences );
		Log.i( getLocalClassName(), "hello there $$$$$$" );
		HttpManager httpManager = new HttpManager( LoginManager.WISERPATH_URI );
		httpManager.postLogin( "anisbet", "password" );
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
		View layout = inflater.inflate( R.layout.custom_toast_layout,
			(ViewGroup) findViewById( R.id.custom_toast_layout_root ) );

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
}