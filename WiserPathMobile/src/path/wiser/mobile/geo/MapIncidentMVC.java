/**
 * 
 */
package path.wiser.mobile.geo;

import path.wiser.mobile.ui.WiserPathActivity;
import path.wiser.mobile.util.PoiList;

/**
 * @author andrewnisbet
 * 
 */
public class MapIncidentMVC extends MapSinglePointMVC
{

	/**
	 * @param poiList
	 * @param itemizedOverlay
	 */
	public MapIncidentMVC( PoiList poiList, WiserPathActivity activity )
	{
		super( poiList, activity );
		// that's it for incidents unless you want to add something fancier later.
	}

}
