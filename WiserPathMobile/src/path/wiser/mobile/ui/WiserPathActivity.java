package path.wiser.mobile.ui;

import java.util.List;

import path.wiser.mobile.R;
import path.wiser.mobile.geo.MapOverlayItems;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

/**
 * This class is the UI for the Wiser Path tab of the application. Note that this class subclasses MapActivity, not
 * {@link path.wiser.mobile.ui.WiserPathHelper}.
 * 
 * @author andrewnisbet
 * 
 */
public class WiserPathActivity extends MapActivity
{

	private List<Overlay>	mapOverlays;
	private Drawable		drawable;
	private MapOverlayItems	itemizedOverlay;

	/** Called when the activity is first created. */
	@Override
	public void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		setContentView( R.layout.wiserpath_tab );
		MapView mapView = (MapView) findViewById( R.id.mapview );
		mapView.setBuiltInZoomControls( true );

		// now instantiate the map's overlay items.
		mapOverlays = mapView.getOverlays();
		drawable = this.getResources().getDrawable( R.drawable.ic_tee_poi_blue );
		itemizedOverlay = new MapOverlayItems( drawable );

		// this places a Wiser path POI 'tee' icon on west ed mall.
		GeoPoint point = new GeoPoint( 53522780, -113623052 ); // WGS84 * 1e6
		OverlayItem overlayitem = new OverlayItem( point, "West Edmonton Mall", "The greatest indoor show on Earth!" );
		itemizedOverlay.addOverlay( overlayitem );
		mapOverlays.add( itemizedOverlay );
	}

	@Override
	protected boolean isRouteDisplayed()
	{
		// TODO Auto-generated method stub
		return false;
	}
}
