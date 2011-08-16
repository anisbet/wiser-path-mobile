/**
 * 
 */
package path.wiser.mobile.util;

import path.wiser.mobile.geo.POI;
import path.wiser.mobile.ui.WiserPathActivity;

import com.google.android.maps.OverlayItem;

/**
 * @author andrewnisbet
 * 
 */
public class MapTraceMVC extends MapBlogMVC
{

	public MapTraceMVC( PoiList poiList, WiserPathActivity activity )
	{
		super( poiList, activity );
	}

	@Override
	protected OverlayItem getLayerItem( POI blog )
	{
		// TODO change this so the super classes layer item is populated correctly.
		// this is how a blog does it:

		// GeoPoint point = new GeoPoint( 53522780, -113623052 ); // WGS84 * 1e6
		// OverlayItem layerItem = new OverlayItem( point, "West Edmonton Mall", "The greatest indoor show on Earth!"
		// );
		// int lat = (int) ( ( (Blog) blog ).getLatitude() * 1E6 );
		// int lon = (int) ( ( (Blog) blog ).getLongitude() * 1E6 );
		// GeoPoint point = new GeoPoint( lat, lon );
		// return new OverlayItem( point, blog.getTitle(), blog.getDescription() );
		return null;

	}

}
