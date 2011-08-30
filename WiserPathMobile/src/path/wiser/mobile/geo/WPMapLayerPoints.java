/**
 * 
 */
package path.wiser.mobile.geo;

import java.util.ArrayList;

import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

/**
 * This class creates and manages the items to be drawn on a google map overlay. This is used by the device to filter
 * for a collection of items.
 * 
 * @author andrewnisbet
 * 
 */
public class WPMapLayerPoints extends ItemizedOverlay<OverlayItem>
{

	protected ArrayList<OverlayItem>	overlayItems	= new ArrayList<OverlayItem>();
	protected MapLayerType				layerDataType;

	public WPMapLayerPoints( Drawable defaultMarker, MapLayerType type )
	{
		// center the icon on the bottom boundary of the image.
		super( boundCenterBottom( defaultMarker ) );
		this.layerDataType = type;
	}

	/**
	 * @param item
	 */
	public void addOverlayItem( OverlayItem item )
	{
		overlayItems.add( item );
		populate();
	}

	@Override
	protected OverlayItem createItem( int i )
	{
		return overlayItems.get( i );
	}

	@Override
	public int size()
	{
		return this.overlayItems.size();
	}

	/**
	 * Clears the list of overlay items.
	 */
	public void clear()
	{
		this.overlayItems.clear();
	}

	/**
	 * @return the layerDataType
	 */
	public final MapLayerType getLayerType()
	{
		return layerDataType;
	}

}
