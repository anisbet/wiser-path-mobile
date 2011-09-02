/**
 * 
 */
package path.wiser.mobile.geo;

import java.util.List;

import path.wiser.mobile.R;
import path.wiser.mobile.ui.LineStyle;
import path.wiser.mobile.ui.WiserPathActivity;
import path.wiser.mobile.util.PoiList;

import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

/**
 * Places multiple traces with multiple points on the map as polylines.
 * 
 * @author andrewnisbet
 * 
 */
public class MapTraceMVC extends MapPointMVC
{

	private MapView		mapView		= null;
	// Line style for polylines -- null will default to Android Green colour. TODO change to WPM colours as required.
	private LineStyle	lineStyle	= null;

	/**
	 * @param poiList
	 * @param activity
	 */
	public MapTraceMVC( PoiList poiList, WiserPathActivity activity )
	{
		super( poiList, activity );
		this.mapView = (MapView) activity.findViewById( R.id.mapview );
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
		// Add all points on the polyline. If you want to change the style of the line change the
		// null parameter to a new path.wiser.mobile.ui.LineStyle.
		Polyline tracePolyline = new Polyline( this.lineStyle, trace );
		map.add( tracePolyline );

		while (trace.getNext() != null) // Loop through the rest of the list.
		{
			trace = (Trace) poiList.next();
			tracePolyline = new Polyline( this.lineStyle, trace );
			map.add( tracePolyline );
		}

	}

}
