/**
 * 
 */
package path.wiser.mobile;

/**
 * Storage for WiserPathEnvironment variables.
 * 
 * @author andrewnisbet
 * 
 */
public final class WPEnvironment
{
	private static boolean	preferExternalStorage	= true;

	/**
	 * @return the preferExternalStorage
	 */
	public static final boolean isPreferExternalStorage()
	{
		return preferExternalStorage;
	}

	/**
	 * @param preferExternalStorage the preferExternalStorage to set
	 */
	public static final void setPreferExternalStorage( boolean preferExternalStorage )
	{
		WPEnvironment.preferExternalStorage = preferExternalStorage;
	}
}
