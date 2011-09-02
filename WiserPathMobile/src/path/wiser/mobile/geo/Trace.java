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
 * A Trace is started on its creation and once stopped it cannot be restarted.
 * 
 * @author andrewnisbet
 * 
 */
public class Trace extends POI implements ComputableTripMetrics
{
	private Vector<Location>		tracePoints		= null;
	private ComputableTripMetrics	traceComputer	= null;
	private boolean					isRunning		= false;
	// Storage for blogs and incidents associated with this Trace.
	private Vector<POI>				associatedBlogs	= null;

	public enum TrailNode // used to identify which node you would like.
	{
		HEAD, /* the list head of the trace */
		TAIL, /* the tail of the trace. */
		PREV_TAIL, /* the penultimate tail */
	}

	public Trace()
	{
		this.setType( POI.Type.TRACE );
		this.tracePoints = new Vector<Location>();
		this.traceComputer = new TraceComputer( tracePoints );
		this.tags = new Tags();
		this.associatedBlogs = new Vector<POI>();
	}

	/**
	 * Adds a Blog or Incident to the Trace.
	 * 
	 * @param blog
	 */
	public void setPoi( Blog blog )
	{
		this.associatedBlogs.add( blog );
	}

	/**
	 * Returns the POIs that are associated with this Trace.
	 * 
	 * @return stored POIs
	 */
	public Vector<POI> getPois()
	{
		return this.associatedBlogs;
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
	public boolean isValid()
	{
		// TODO compute validity of Trace
		return false;
	}

	/**
	 * @return
	 */
	public Vector<Location> getLocations()
	{
		return this.tracePoints;
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

	/**
	 * @return true if no locations have been recorded and false otherwise.
	 */
	public boolean needsLocation()
	{
		return this.tracePoints.size() == 0;
	}

	/**
	 * @return true if the trace is currently running and false otherwise.
	 */
	public boolean isRunning()
	{
		return this.isRunning;
	}

	/**
	 * Stops the recording of the trace.
	 */
	public void stop()
	{
		// TODO Implement stop

	}

	/**
	 * @return true if there are related blogs and false otherwise.
	 */
	public boolean hasRelatedPOIs()
	{
		return this.associatedBlogs.size() > 0;
	}

	/**
	 * @return Location of first point recorded.
	 */
	public Location getInitialLocation()
	{
		// Could be null if no points recorded.
		return this.tracePoints.firstElement();
	}

}
