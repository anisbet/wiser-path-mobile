package path.wiser.mobile.util;

import path.wiser.mobile.geo.Blog;
import path.wiser.mobile.geo.WPMapLayerItems;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

/**
 * Places the Blog on the appropriate location on the map.
 * 
 * @author andrewnisbet
 * 
 */
public class MapBlogMVC implements ModelViewController
{
	protected Blog				blog;
	protected WPMapLayerItems	itemizedOverlay;

	/**
	 * @param blog
	 * @param whichOverlay The over lay we add the items to.
	 */
	public MapBlogMVC( Blog blog, WPMapLayerItems whichOverlay )
	{
		this.blog = blog;
		this.itemizedOverlay = whichOverlay;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see path.wiser.mobile.util.ModelViewController#update()
	 */
	@Override
	public void update()
	{
		int lat = (int) ( this.blog.getLatitude() * 1E6 );
		int lon = (int) ( this.blog.getLongitude() * 1E6 );
		GeoPoint point = new GeoPoint( lat, lon );
		OverlayItem overlayItem = new OverlayItem( point, this.blog.getTitle(), this.blog.getDescription() );
		itemizedOverlay.addOverlayItem( overlayItem );
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
