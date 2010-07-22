/**
 * 
 */
package path.wiser.mobile.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * This class represents the Point of Interest screen on the Android.
 * @author andrew nisbet
 *
 */
public class PointOfInterestActivity extends Activity {
	protected String greeting = "This is the POI tab";
	public void  onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

        TextView textview = new TextView(this);
        textview.setText(greeting);
        setContentView(textview);
	}
}
