/**
 * 
 */
package path.wiser.mobile.geo;

import android.location.Location;

/**
 * Blog is like a POI except that it includes an image.
 * 
 * @author andrewnisbet
 * 
 */
public class Blog extends POI
{
	// a blog has a single location.
	private Location	location	= null;

	// TODO include image

	/**
	 * @param location the location to set
	 */
	public void setLocation( Location location )
	{
		this.location = location;
	}

	/**
	 * @return True if the blog has a stored location and false otherwise.
	 */
	public boolean needsLocation()
	{
		return this.location == null;
	}

}
