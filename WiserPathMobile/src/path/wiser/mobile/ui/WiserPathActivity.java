package path.wiser.mobile.ui;

import path.wiser.mobile.R;
import android.os.Bundle;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

public class WiserPathActivity extends MapActivity
{
	/** Called when the activity is first created. */
	@Override
	public void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		setContentView( R.layout.wiserpath_tab );
		MapView mapView = (MapView) findViewById( R.id.mapview );
		mapView.setBuiltInZoomControls( true );
	}

	@Override
	protected boolean isRouteDisplayed()
	{
		// TODO Auto-generated method stub
		return false;
	}
}
