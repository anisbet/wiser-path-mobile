package path.wiser.mobile;

import android.app.Activity;
import android.os.Bundle;

public class TraceActivity extends Activity
{
	/** Called when the activity is first created. */
	@Override
	public void onCreate( Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trace_tab);
	}
}