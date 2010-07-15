/**
 * 
 */
package path.wiser.mobile;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * @author andrewnisbet
 *
 */
public class SettingsActivity extends Activity
{
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);

        TextView textview = new TextView(this);
        textview.setText("This is the Settings tab");
        setContentView(textview);
    }
}
