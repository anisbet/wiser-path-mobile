/**
 * 
 */
package path.wiser.mobile.geo;

import android.location.Location;

/**
 * This class emulates Android's Location object but allows for
 * storing the object to XML rather than just Parceling the information to disk.
 * 
 * @author andrewnisbet
 * 
 */
public class WPMLocation
{

	private double		longitude;
	private double		latitude;
	private float		speed;
	private Location	location;
	private long		time;

	/**
	 * @param location as a comma separated string as in KML coordinates.
	 */
	public WPMLocation( String location )
	{
		this.time = System.currentTimeMillis();
	}

	public WPMLocation( Location location )
	{
		this.time = System.currentTimeMillis();
		this.location = location;
	}

	public double distanceTo( WPMLocation location )
	{
		if (location != null) // could be null if this object was reserialized from disk.
		{
			return this.location.distanceTo( location.getLocation() );
		}
		return 0.0; // unless we can compute the distance cheaply.
	}

	public float getBearing()
	{
		if (location != null)
		{
			return this.location.getBearing();
		}
		return 0.0f;
	}

	private Location getLocation()
	{
		return this.location;
	}

	public double getLatitude()
	{
		return this.latitude;
	}

	public double getLongitude()
	{
		return this.longitude;
	}

	public float getSpeed()
	{
		return this.speed;
	}

	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude( double longitude )
	{
		this.longitude = longitude;
	}

	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude( double latitude )
	{
		this.latitude = latitude;
	}

	/**
	 * @param speed the speed to set
	 */
	public void setSpeed( float speed )
	{
		this.speed = speed;
	}

	/**
	 * @return the time
	 */
	public long getTime()
	{
		return time;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime( long time )
	{
		this.time = time;
	}

}
