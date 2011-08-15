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
public class WPMapLayerItems extends ItemizedOverlay<OverlayItem>
{

	private ArrayList<OverlayItem>	overlayItems	= new ArrayList<OverlayItem>();
	private MapLayerType			layerDataType;

	public WPMapLayerItems( Drawable defaultMarker, MapLayerType type )
	{
		// center the icon on the bottom boundary of the image.
		super( boundCenterBottom( defaultMarker ) );
		this.layerDataType = type;
	}

	/**
	 * @param overlay
	 */
	public void addOverlayItem( OverlayItem overlay )
	{
		overlayItems.add( overlay );
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
