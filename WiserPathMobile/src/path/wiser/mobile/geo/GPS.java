/**
 * 
 */
package path.wiser.mobile.geo;

import path.wiser.mobile.Units;
import path.wiser.mobile.ui.PointOfInterestActivity;
import path.wiser.mobile.ui.TraceActivity;
import path.wiser.mobile.ui.WiserActivityHelper;
import android.content.Context;
import android.location.LocationListener;
import android.location.LocationManager;

/**
 * @author andrewnisbet
 * 
 */
public class GPS
{

	private LocationManager	locationManager;

	/**
	 * Use this constructor if you want to use the lowest possible granularity of location and time interval
	 * of updates to location.
	 */
	public GPS( PointOfInterestActivity activity ) // incident activity extends PointOfInterestActivity
	{
		this.locationManager = (LocationManager) activity.getSystemService( Context.LOCATION_SERVICE );
		// TODO in version 2.3.1 and later there is a method to requestSingleUpdate() which would be more efficient.
		locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0L, 0.0f, activity );
	}

	/**
	 * Use this constructor if you want to set a reasonable polling time and or distance of geographic location.
	 * 
	 * @param activty the trace activity.
	 */
	public GPS( TraceActivity activity )
	{
		long minimumTime = computeMinimumTime();
		float minDistance = computeMinimumDistance();
		this.locationManager = (LocationManager) activity.getSystemService( Context.LOCATION_SERVICE );
		locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, minimumTime, minDistance, activity );
	}

	/**
	 * Disconnects the update listener from the GPS.
	 * 
	 * @param listener The WiserPathActivityHelper that wishes to stop listening for updates.
	 */
	public void stopUpdatingLocation( WiserActivityHelper listener )
	{
		// Remove the listener you previously added
		this.locationManager.removeUpdates( (LocationListener) listener );
	}

	/**
	 * @return best minimum distance for recording a location -- used to save power on the device.
	 *         Distance is always in metres.
	 */
	private float computeMinimumDistance()
	{
		switch (Units.getActivityType())
		{
		case DRIVE:
			return 50.0f;
		case WALK:
			return 10.0f;
		case RUN:
			// return 10.0f;
		case BIKE:
			// return 10.0f;
		default:
			return 10.0f;
		}
	}

	/**
	 * @return best minimum time for recording a location -- used to save power on the device.
	 *         Time is returned in milliseconds.
	 */
	private long computeMinimumTime()
	{
		switch (Units.getActivityType())
		{
		case DRIVE:
			return 500L;
		case WALK:
			return 10000L;
		case RUN:
			// return 1000L;
		case BIKE:
			// return 1000L;
		default:
			return 1000L;
		}
	}

}
