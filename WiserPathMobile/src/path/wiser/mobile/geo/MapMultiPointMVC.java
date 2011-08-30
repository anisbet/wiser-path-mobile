/**
 * 
 */
package path.wiser.mobile.geo;

import java.util.List;

import path.wiser.mobile.ui.WiserPathActivity;
import path.wiser.mobile.util.PoiList;
import android.location.Location;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

/**
 * @author andrewnisbet
 * 
 */
public class MapMultiPointMVC extends MapSinglePointMVC
{

	public MapMultiPointMVC( PoiList poiList, WiserPathActivity activity )
	{
		super( poiList, activity );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see path.wiser.mobile.util.ModelViewController#update()
	 */
	@Override
	public void update()
	{
		// all updates start with taking each of the POIs and doing the following. The only difference is
		// Traces get layer items in a different way to the other POIs.
		if (poiList.isEmpty())
		{
			return;
		}

		List<Overlay> map = activity.getMap();
		Trace trace = (Trace) poiList.getCurrent();
		// call super getLayerItem() for first point.
		// OverlayItem overlayItem = getLayerItem( trace );
		// layer.addOverlayItem( overlayItem );
		// Add all points on the polyline
		drawPolyLine( layer, trace );

		while (trace.getNext() != null) // Loop through the rest of the list.
		{
			trace = (Trace) poiList.next();
			// overlayItem = getLayerItem( trace );
			// layer.addOverlayItem( overlayItem );
			drawPolyLine( layer, trace );
		}

		map.add( layer );

	}

	private void drawPolyLine( WPMapLayerPoints layer, Trace trace )
	{
		// TODO change this so the super classes layer item is populated correctly.
		// this is how a blog does it:

		// GeoPoint point = new GeoPoint( 53522780, -113623052 ); // WGS84 * 1e6
		// OverlayItem layerItem = new OverlayItem( point, "West Edmonton Mall", "The greatest indoor show on Earth!" );
		int lat, lon;
		GeoPoint point;
		for (Location location : trace.getLocations())
		{
			lat = (int) ( location.getLatitude() * 1E6 );
			lon = (int) ( location.getLongitude() * 1E6 );
			point = new GeoPoint( lat, lon );
			layer.addOverlayItem( new OverlayItem( point, trace.getTitle(), trace.getDescription() ) );
		}

	}

}
