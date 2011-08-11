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
public class WPMapOverlayItems extends ItemizedOverlay<OverlayItem>
{

	private ArrayList<OverlayItem>	overlays	= new ArrayList<OverlayItem>();

	public WPMapOverlayItems( Drawable defaultMarker )
	{
		// center the icon on the bottom boundary of the image.
		super( boundCenterBottom( defaultMarker ) );
	}

	/**
	 * @param overlay
	 */
	public void addOverlayItem( OverlayItem overlay )
	{
		overlays.add( overlay );
		populate();
	}

	@Override
	protected OverlayItem createItem( int i )
	{
		return overlays.get( i );
	}

	@Override
	public int size()
	{
		return this.overlays.size();
	}

}
