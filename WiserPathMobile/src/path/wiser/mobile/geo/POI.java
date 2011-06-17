/**
 * 
 */
package path.wiser.mobile.geo;

import path.wiser.mobile.Tags;
import android.location.Location;

/**
 * @author andrewnisbet
 * 
 */
public abstract class POI
{
	protected String	title		= "";
	protected String	description	= "";

	public abstract void setLocation( Location location );

	/**
	 * @return the title
	 */
	public String getPoiTitle()
	{
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setPoiTitle( String title )
	{
		this.title = title;
	}

	/**
	 * @return the tags
	 */
	public abstract Tags getTags();

	/**
	 * @param tags the tags to set
	 */
	public abstract void setTags( String tags );

	/**
	 * @param blog
	 */
	public void setDescription( String blog )
	{
		this.description = blog;
	}

	/**
	 * @return The description of the trace or the Blog's content.
	 */
	public String getDescription()
	{
		return this.description;
	}

	/**
	 * Checks if the object is valid, that is, is all the information
	 * that WiserPath is expecting filled out in the object.
	 * 
	 * @return True if this is a valid object and false otherwise.
	 */
	public abstract boolean validate();

}
