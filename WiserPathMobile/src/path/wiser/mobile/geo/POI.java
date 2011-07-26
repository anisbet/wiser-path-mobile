/**
 * 
 */
package path.wiser.mobile.geo;

import path.wiser.mobile.util.Tags;
import android.location.Location;

/**
 * Note on Serialization:
 * POI objects use KML to serialize their data to file.
 * 
 * @author andrewnisbet
 * 
 */
public abstract class POI implements Comparable<POI>
{
	public enum Type
	{
		BLOG, TRACE, INCIDENT
	}

	protected String	title		= "";
	protected String	description	= "";
	protected boolean	isUploaded	= false;
	protected Tags		tags		= null;
	private POI			previous	= null;
	private POI			next		= null;
	private Type		type;

	/**
	 * @return the isUploaded
	 */
	public boolean isUploaded()
	{
		return isUploaded;
	}

	/**
	 * @param isUploaded the isUploaded to set
	 */
	public void setUploaded( boolean isUploaded )
	{
		this.isUploaded = isUploaded;
	}

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
	public void setTitle( String title )
	{
		this.title = title;
	}

	/**
	 * @param tags the tags to set this POI to.
	 */
	public void setTags( Tags tags )
	{
		this.tags = tags;
	}

	/**
	 * @return false for Blogs, Incidents which extend this class return true.
	 */
	public boolean isIncident()
	{
		return false;
	}

	/**
	 * @return Tags for this POI.
	 */
	public Tags getTags()
	{
		return this.tags;
	}

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo( POI poi )
	{
		return this.title.compareTo( poi.title );
	}

	/**
	 * @return the previous
	 */
	public POI getPrevious()
	{
		return previous;
	}

	/**
	 * @return the next
	 */
	public POI getNext()
	{
		return next;
	}

	/**
	 * @param previous the previous to set
	 */
	public void setPrevious( POI previous )
	{
		this.previous = previous;
	}

	/**
	 * @param next the next to set
	 */
	public void setNext( POI next )
	{
		this.next = next;
	}

	/**
	 * @return the type
	 */
	public Type getType()
	{
		return type;
	}

	/**
	 * @param type the type to set
	 */
	protected void setType( Type type )
	{
		this.type = type;
	}

}
