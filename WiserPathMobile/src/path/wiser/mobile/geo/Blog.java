/**
 * 
 */
package path.wiser.mobile.geo;

import java.io.File;

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
	private Tags		tags		= null;
	private String		imagePath	= null;
	private ImageType	extension;

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

	/**
	 * @return True if there is an image associated with this blog and false otherwise.
	 */
	public boolean hasImage()
	{
		return this.imagePath != null && this.imagePath.length() > 0;
	}

	/**
	 * @param imagePath the imagePath to set
	 */
	public void setImagePath( String imagePath )
	{
		this.imagePath = imagePath;
	}

	/**
	 * @return the imagePath
	 */
	public String getImagePath()
	{
		return imagePath;
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
		String[] pathParts = this.imagePath.split( File.pathSeparator );
		if (pathParts.length > 0)
		{
			// TODO test this.
			String[] fileParts = pathParts[pathParts.length - 1].split( "." );
			if (fileParts.length > 0)
			{
				this.setExtension( fileParts[fileParts.length - 1].toLowerCase() );
			}
			return pathParts[pathParts.length - 1];
		}

		return "";
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

}
