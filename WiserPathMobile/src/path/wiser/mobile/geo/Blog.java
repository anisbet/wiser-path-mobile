/**
 * 
 */
package path.wiser.mobile.geo;

import path.wiser.mobile.util.ImageType;
import path.wiser.mobile.util.Tags;
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
	private String		imageName	= null;
	private ImageType	extension;
	protected boolean	isIncident;

	// TODO include image
	public Blog()
	{
		this.setType( POI.Type.BLOG );
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
	public boolean isValid()
	{
		return this.title.length() > 0 && this.description.length() > 0 && ( needsLocation() == false );
	}

	/**
	 * @return True if there is an image associated with this blog and false otherwise.
	 */
	public boolean hasImage()
	{
		return this.imageName != null && this.imageName.length() > 0;
	}

	/**
	 * @param name the imageName to set
	 */
	public void setImageName( String name )
	{
		this.imageName = name;
	}

	/**
	 * @return the imageName
	 */
	public String getImagePath()
	{
		return imageName;
	}

	/**
	 * @return The location as a string like POINT(-12661258.674479%207092974.0387356)
	 *         but may be altered in the future to be lat long
	 */
	public String getLocation()
	{
		// return "POINT(" + String.valueOf( this.location.getLatitude() ) + "%20" + String.valueOf(
		// this.location.getLongitude() ) + ")";
		// TODO Get info from Ranek about how to handle this.
		return "POINT(-12661258.674479%207092974.0387356)";
	}

	/**
	 * Used to return just the lat and long as a comma separated pair.
	 * 
	 * @return lat long pair separated by a comma.
	 */
	public String getCoordinates()
	{
		return String.valueOf( location.getLatitude() ) + "," + String.valueOf( location.getLongitude() );
	}

	/**
	 * @return name of the image.
	 */
	public String getImageName()
	{
		return this.imageName;
	}

	/**
	 * @param extension the extension to set
	 */
	public void setExtension( String extension )
	{
		if (extension.equalsIgnoreCase( "jpg" ) || extension.equalsIgnoreCase( "jpeg" ))
		{
			this.extension = ImageType.JPG;
		}
		else
		{
			this.extension = ImageType.UNSUPPORTED;
		}
	}

	/**
	 * @return the extension
	 */
	public ImageType getExtension()
	{
		return extension;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see path.wiser.mobile.geo.POI#setIsIncident(java.lang.String)
	 */
	public void setIsIncident( String isIncident )
	{
		this.isIncident = Boolean.parseBoolean( isIncident );
	}

	/**
	 * @return double latitude.
	 */
	public double getLatitude()
	{
		return this.location.getLatitude();
	}

	/**
	 * @return
	 */
	public double getLongitude()
	{
		return this.location.getLongitude();
	}

}
