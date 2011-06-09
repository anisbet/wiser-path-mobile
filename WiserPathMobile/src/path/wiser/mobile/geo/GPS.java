/**
 * 
 */
package path.wiser.mobile.geo;

import path.wiser.mobile.ui.WiserActivityHelper;
import android.content.Context;
import android.location.LocationManager;

/**
 * @author andrewnisbet
 * 
 */
public class GPS
{
	/**
	 * Use this constructor if you want to use the lowest possible granularity of location and time interval
	 * of updates to location.
	 */
	public GPS( WiserActivityHelper activity )
	{
		init( 0L, 0f, activity );
	}

	/**
	 * Use this constructor if you want to set a reasonable polling time and or distance of geographic location.
	 * 
	 * @param minimumTime time in milliseconds
	 * @param minDistance distance in meters.
	 */
	public GPS( long minimumTime, float minDistance, WiserActivityHelper activity )
	{
		init( minimumTime, minDistance, activity );
	}

	/**
	 * Performs common startup activities for the GPS.
	 * 
	 * @param minTime
	 * @param minDist
	 */
	private void init( long minTime, float minDist, WiserActivityHelper activity )
	{
		LocationManager locationManager = (LocationManager) activity.getSystemService( Context.LOCATION_SERVICE );
		// TODO test if we got the location Manager if not we need
		locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, minTime, minDist, activity );
	}

}
