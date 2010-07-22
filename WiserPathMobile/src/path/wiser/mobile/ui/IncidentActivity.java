/**
 * 
 */
package path.wiser.mobile.ui;

import android.os.Bundle;
import android.widget.TextView;

/**
 * @author andrew nisbet
 *
 */
public class IncidentActivity extends PointOfInterestActivity {
	
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	greeting = "This is the Incident tab";
        TextView textview = new TextView(this);
        textview.setText(greeting);
        setContentView(textview);
    }
}
