/**
 * 
 */
package path.wiser.mobile.util;

/**
 * @author anisbet
 * 
 */
public enum ImageType
{
	UNSUPPORTED, JPG; // needed to delineate toString method.

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString()
	{
		// send the lower case version of the enum
		String s = super.toString();
		return "." + s.toLowerCase();
	}
}
