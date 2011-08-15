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
	protected PoiList			blogList;
	protected WiserPathActivity	activity;

	/**
	 * @param poiList.
	 * @param activity The over lay we add the items to.
	 */
	public MapBlogMVC( PoiList poiList, WiserPathActivity activity )
	{
		this.blogList = poiList;
		this.activity = activity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see path.wiser.mobile.util.ModelViewController#update()
	 */
	@Override
	public void update()
	{
		if (blogList.isEmpty())
		{
			return;
		}

		Drawable blogIcon = activity.getResources().getDrawable( R.drawable.ic_tee_poi_blue );
		WPMapLayerItems blogOverlay = new WPMapLayerItems( blogIcon, MapLayerType.MOBILE_BLOG );
		List<Overlay> map = activity.getMap();

		POI currentPoi = blogList.getCurrent();
		OverlayItem overlayitem = getLayerItem( currentPoi );
		blogOverlay.addOverlayItem( overlayitem );

		while (currentPoi.getNext() != null) // Loop through the rest of the list.
		{
			currentPoi = blogList.next();
			overlayitem = getLayerItem( currentPoi );
			blogOverlay.addOverlayItem( overlayitem );
		}

		map.add( blogOverlay );

	}

	/**
	 * @param blog
	 * @return the formatted Blog as an OverlayItem
	 */
	private OverlayItem getLayerItem( POI blog )
	{
		// GeoPoint point = new GeoPoint( 53522780, -113623052 ); // WGS84 * 1e6
		// OverlayItem overlayitem = new OverlayItem( point, "West Edmonton Mall", "The greatest indoor show on Earth!"
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
		// This has no meaning currently, but would be useful if you change something on the map to reflect the model.

	}

}
