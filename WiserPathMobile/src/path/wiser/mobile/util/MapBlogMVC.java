package path.wiser.mobile.util;

import java.util.List;

import path.wiser.mobile.R;
import path.wiser.mobile.geo.Blog;
import path.wiser.mobile.geo.MapLayerType;
import path.wiser.mobile.geo.POI;
import path.wiser.mobile.geo.WPMapLayerItems;
import path.wiser.mobile.ui.WiserPathActivity;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

/**
 * Places the Blog on the appropriate location on the map.
 * 
 * @author andrewnisbet
 * 
 */
public class MapBlogMVC implements ModelViewController
{
	protected PoiList			poiList		= null;
	protected WiserPathActivity	activity	= null;
	protected Drawable			icon		= null;
	protected WPMapLayerItems	layer		= null;

	/**
	 * @param poiList.
	 * @param activity The over lay we add the items to.
	 */
	public MapBlogMVC( PoiList poiList, WiserPathActivity activity )
	{
		this.poiList = poiList;
		this.activity = activity;
		switch (poiList.getType())
		{
		case BLOG:
			icon = activity.getResources().getDrawable( R.drawable.ic_tee_poi_blue );
			layer = new WPMapLayerItems( icon, MapLayerType.MOBILE_BLOG );
			break;

		case INCIDENT:
			icon = activity.getResources().getDrawable( R.drawable.icon );
			layer = new WPMapLayerItems( icon, MapLayerType.MOBILE_INCIDENT );
			break;

		case TRACE: // test what is required for a trace.
			icon = activity.getResources().getDrawable( R.drawable.icon ); // not sure about implications of this.
			layer = new WPMapLayerItems( icon, MapLayerType.MOBILE_TRACE );
			break;

		default:
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see path.wiser.mobile.util.ModelViewController#update()
	 */
	@Override
	public void update()
	{
		if (poiList.isEmpty())
		{
			return;
		}

		List<Overlay> map = activity.getMap();
		POI currentPoi = poiList.getCurrent();
		OverlayItem overlayitem = getLayerItem( currentPoi );
		layer.addOverlayItem( overlayitem );

		while (currentPoi.getNext() != null) // Loop through the rest of the list.
		{
			currentPoi = poiList.next();
			overlayitem = getLayerItem( currentPoi );
			layer.addOverlayItem( overlayitem );
		}

		map.add( layer );

	}

	/**
	 * @param blog
	 * @return the formatted Blog as an OverlayItem
	 */
	protected OverlayItem getLayerItem( POI blog )
	{
		// GeoPoint point = new GeoPoint( 53522780, -113623052 ); // WGS84 * 1e6
		// OverlayItem layer = new OverlayItem( point, "West Edmonton Mall", "The greatest indoor show on Earth!"
		// );
		int lat = (int) ( ( (Blog) blog ).getLatitude() * 1E6 );
		int lon = (int) ( ( (Blog) blog ).getLongitude() * 1E6 );
		GeoPoint point = new GeoPoint( lat, lon );
		return new OverlayItem( point, blog.getTitle(), blog.getDescription() );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see path.wiser.mobile.util.ModelViewController#change()
	 */
	@Override
	public void change()
	{
		// This has no meaning currently, but would be useful if you wanted to let the user
		// drag their POIs on the map -- food for thought.

	}

}
