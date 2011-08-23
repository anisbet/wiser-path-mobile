/**
 * 
 */
package path.wiser.mobile;

import path.wiser.mobile.util.ImageType;

/**
 * Storage for WiserPathEnvironment variables.
 * 
 * @author andrewnisbet
 * 
 */
public final class WPEnvironment
{

	/**
	 * Default image quality for JPEG, the value is ignored if we save as PNG.
	 */
	public static final int		DEFAULT_IMAGE_QUALITY	= 50;
	public static final String	TRACE_PATH				= "/trace";
	public static final String	BLOG_PATH				= "/blog";
	public static final String	INCIDENT_PATH			= "/incident";
	public static final String	TRACE_UPLOAD_EXTENSION	= ".gpx";
	private static boolean		preferExternalStorage	= true;
	private static ImageType	imageType				= ImageType.JPG;

	/**
	 * @return the preferExternalStorage
	 */
	public static final boolean isPreferExternalStorage()
	{
		return preferExternalStorage;
	}

	/**
	 * @param preferExternalStorage the preferExternalStorage to set.
	 */
	public static final void setPreferExternalStorage( boolean preferExternalStorage )
	{
		WPEnvironment.preferExternalStorage = preferExternalStorage;
	}

	/**
	 * Sets the preferred image type used by Wiser Path Mobile.
	 * Currently only JPG is supported.
	 * 
	 * @param type
	 */
	public static void setImageType( ImageType type )
	{
		WPEnvironment.imageType = type;
	}

	/**
	 * that will be used applciation wide. Currently only JPG is supported.
	 * 
	 * @return ImageType as a String in lower case.
	 */
	public static String getImageExtension()
	{
		return WPEnvironment.imageType.toString();
	}
}
