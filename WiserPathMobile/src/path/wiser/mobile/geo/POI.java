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
	protected GPS		gps		= null;

	protected String	title	= "";
	protected Tags		tags	= null;

	private String		blog	= "";

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
	public Tags getTags()
	{
		return tags;
	}

	/**
	 * @param tags the tags to set
	 */
	public void setTags( Tags tags )
	{
		this.tags = tags;
	}

	public void setBlog( String blog )
	{
		this.blog = blog;
	}

	public String getBlog()
	{
		return this.blog;
	}

}
