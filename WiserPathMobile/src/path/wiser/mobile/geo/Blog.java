/**
 * 
 */
package path.wiser.mobile.geo;

import path.wiser.mobile.Tags;
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
	private Tags		tags		= null;

	// TODO include image
	public Blog()
	{
		this.tags = new Tags();
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation( Location location )
	{
		this.location = location;
	}

	/**
	 * @return True if the description has a stored location and false otherwise.
	 */
	public boolean needsLocation()
	{
		return this.location == null;
	}

	/**
	 * This method ensures that the minimal information for a POI in Wiser Path
	 * has been met. The GeoBlog must have a title, location and text body.
	 */
	public boolean validate()
	{
		return this.title.length() > 0 && this.description.length() > 0 && ( needsLocation() == false );
	}

	@Override
	public Tags getTags()
	{
		return this.tags;
	}

	@Override
	public void setTags( String tags )
	{
		this.tags.setTags( tags );
	}
}
