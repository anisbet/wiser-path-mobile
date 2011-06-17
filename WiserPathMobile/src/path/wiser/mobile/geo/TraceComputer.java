/**
 * 
 */
package path.wiser.mobile.geo;

import java.util.Vector;

import path.wiser.mobile.Units;
import android.location.Location;

/**
 * This class is a strategy type pattern for Traces and does the heavy lifting for computing the following metrics:
 * <ol>
 * <li>speed
 * <li>distance
 * <li>lat
 * <li>long
 * <li>time
 * <li>direction
 * <li>pace
 * </ol>
 * 
 * @author andrewnisbet
 * 
 */
public class TraceComputer implements ComputableTripMetrics
{
	private Vector<Location>	locations	= null;

	public TraceComputer( Vector<Location> locations )
	{
		this.locations = locations;
	}

	@Override
	public String getSpeed()
	{
		float value = this.locations.lastElement().getSpeed();
		return String.valueOf( value ) + Units.speed();
	}

	@Override
	public String getDistance()
	{
		double value = this.locations.firstElement().distanceTo( this.locations.lastElement() );
		// or should this be the total from point to point.
		return String.valueOf( value ) + Units.distance();
	}

	@Override
	public String getLatitude()
	{
		double value = this.locations.lastElement().getLatitude();
		// do we need to convert to degrees minutes seconds.
		return String.valueOf( value ) + Units.coordinate( value, true );
	}

	@Override
	public String getLongtitude()
	{
		double value = this.locations.lastElement().getLongitude();
		// do we need to convert to degrees minutes seconds.
		return String.valueOf( value ) + Units.coordinate( value, false );
	}

	@Override
	public String getEllapseTime()
	{
		long value = this.locations.lastElement().getTime() - this.locations.firstElement().getTime();
		// this will now contain the difference in milliseconds.
		return String.valueOf( value ) + Units.time();
	}

	@Override
	public String getDirection()
	{
		float value = this.locations.lastElement().getBearing();
		return String.valueOf( value ) + Units.bearing();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see path.wiser.mobile.geo.ComputableTripMetrics#getPace()
	 */
	@Override
	public String getPace()
	{
		// pace is the difference in speed from the last location.
		float pace = 0.0f;

		if (this.locations.size() > 0)
		{
			if (this.locations.size() > 1)
			{
				// get speed from the second last element and subtract the speed at the last element -- it may be neg or
				// 0 or pos.
				pace = this.locations.get( this.locations.size() - 2 ).getSpeed() - this.locations.lastElement().getSpeed();
			}
			else
			// there is only one element so return its speed because the delta from 0.0 is the current speed.
			{
				pace = this.locations.firstElement().getSpeed();
			}
		}
		// if no locations recorded the pace is 0.0.
		return String.valueOf( pace ) + Units.pace();
	}

}
