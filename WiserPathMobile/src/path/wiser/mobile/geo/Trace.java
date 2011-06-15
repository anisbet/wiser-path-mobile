/**
 * 
 */
package path.wiser.mobile.geo;

import java.util.Vector;

import android.location.Location;
import android.os.Bundle;

/**
 * A Trace is a set of Locations that together make a route.
 * 
 * @author andrewnisbet
 * 
 */
public class Trace extends POI implements ComputableTripMetrics
{
	private Vector<Location>		tracePoints		= null;
	private ComputableTripMetrics	traceComputer	= null;

	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		this.tracePoints = new Vector<Location>();
		this.gps = new GPS( this ); // set up a new GPS. We need to be able to control how often we collect updates to
									// location.
		this.traceComputer = new TraceComputer( tracePoints ); // TODO get this from settings.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.location.LocationListener#onLocationChanged(android.location.Location)
	 */
	@Override
	public void onLocationChanged( Location location )
	{
		// get UTC time and set the locations time
		location.setTime( System.currentTimeMillis() );
		// float speed = this.tracePoints.lastElement().
		// location.setSpeed( speed );
		this.tracePoints.add( location );
	}

	@Override
	public String getSpeed()
	{
		return this.traceComputer.getSpeed();
	}

	@Override
	public String getDistance()
	{
		return this.traceComputer.getDistance();
	}

	@Override
	public String getLatitude()
	{
		return this.traceComputer.getLatitude();
	}

	@Override
	public String getLongtitude()
	{
		return this.traceComputer.getLongtitude();
	}

	@Override
	public String getEllapseTime()
	{
		return this.traceComputer.getEllapseTime();
	}

	@Override
	public String getDirection()
	{
		return this.traceComputer.getDirection();
	}

	@Override
	public String getPace()
	{
		return this.traceComputer.getPace();
	}

}
