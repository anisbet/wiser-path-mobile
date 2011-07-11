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
	protected String	title		= "";
	protected String	description	= "";
	protected boolean	isUploaded	= false;

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

	// Example of a serialized POI
	// <?xml version="1.0" encoding="UTF-8"?>
	// <kml xmlns="http://www.opengis.net/kml/2.2">
	// <Document>
	// <Placemark>
	// <name>Entity references example</name>
	// <description>
	// &lt;h1&gt;Entity references are hard to type!&lt;/h1&gt;
	// &lt;p&gt;&lt;font color="green"&gt;Text is
	// &lt;i&gt;more readable&lt;/i&gt;
	// and &lt;b&gt;easier to write&lt;/b&gt;
	// when you can avoid using entity references.&lt;/font&gt;&lt;/p&gt;
	// </description>
	// <Point>
	// <coordinates>102.594411,14.998518</coordinates>
	// </Point>
	// </Placemark>
	// </Document>
	// </kml>

	/**
	 * Write blog to media.
	 * 
	 * @return true if the serialization was successful and false otherwise.
	 */
	public abstract boolean serialize();

	/**
	 * Retrieve blog from file.
	 * 
	 * @return true if the POI was retrieved from media and false otherwise.
	 */
	public abstract boolean deserialize();

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

}
