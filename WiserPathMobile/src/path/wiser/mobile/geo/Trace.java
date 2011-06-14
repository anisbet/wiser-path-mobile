/**
 * 
 */
package path.wiser.mobile.geo;

import java.util.Vector;

import path.wiser.mobile.Units;
import android.location.Location;

/**
 * @author andrewnisbet
 * 
 */
public class Trace extends POI
{
	private Vector<Location>	tracePoints		= null;
	private TraceComputer		traceComputer	= null;

	public Trace()
	{
		this.tracePoints = new Vector<Location>();
		this.gps = new GPS( this ); // set up a new GPS. We need to be able to control how often we collect updates to
									// location.
		this.traceComputer = new TraceComputer( Units.METRIC, tracePoints ); // TODO get this from settings.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.location.LocationListener#onLocationChanged(android.location.Location)
	 */
	@Override
	public void onLocationChanged( Location arg0 )
	{
		this.tracePoints.add( arg0 );
		// compute the data for trace screen
		refreshListenerScreen();
	}

	public void refreshListenerScreen()
	{

	}

}
