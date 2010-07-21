/**
 * 
 */
package path.wiser.mobile;

import android.os.Bundle;
import android.preference.PreferenceActivity;

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
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);

		// Check if user has access to WIFI?
		// Does user have access through phone?
		// does user want access via either?
		// does their user name and password authenticate?
	}
}