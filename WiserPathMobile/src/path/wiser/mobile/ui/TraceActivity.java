package path.wiser.mobile.ui;

import path.wiser.mobile.R;
import android.app.Activity;
import android.os.Bundle;

/**
 * @author anisbet
 *         This class represents the trace screen.
 */
public class TraceActivity extends Activity
{
	/** Called when the activity is first created. */
	@Override
	public void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		setContentView( R.layout.trace_tab );
	}

}