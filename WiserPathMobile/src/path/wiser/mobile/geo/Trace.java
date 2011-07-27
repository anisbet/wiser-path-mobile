/**
 * 
 */
package path.wiser.mobile.geo;

import java.util.Vector;

import path.wiser.mobile.util.Tags;
import android.location.Location;

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

	public enum TrailNode // used to identify which node you would like.
	{
		HEAD, TAIL, PREV_TAIL,
	}

	public Trace()
	{
		this.setType( POI.Type.TRACE );
		this.tracePoints = new Vector<Location>();
		this.traceComputer = new TraceComputer( tracePoints );
		this.tags = new Tags();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see path.wiser.mobile.geo.POI#setLocation(android.location.Location)
	 */
	@Override
	public void setLocation( Location location )
	{
		// get UTC time and set the locations time
		location.setTime( System.currentTimeMillis() ); // this now done with WPMLocation
		// float speed = computeSpeed();
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

	@Override
	public boolean validate()
	{
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @return all the coordinates as a list of lat long in a big string.
	 */
	public String getCoordinates()
	{
		StringBuffer buffer = new StringBuffer();
		for (Location location : this.tracePoints)
		{
			buffer.append( String.valueOf( location.getLatitude() ) );
			buffer.append( "," );
			buffer.append( String.valueOf( location.getLongitude() ) );
			buffer.append( "\n" );
		}
		return buffer.toString();
	}

	@Override
	public void setIsIncident( String isIncident )
	{
		// this method has no meaning currently, but this flag is stored when the object
		// is serialized. It does have meaning for Blogs and Incidents.
		return;
	}

}
