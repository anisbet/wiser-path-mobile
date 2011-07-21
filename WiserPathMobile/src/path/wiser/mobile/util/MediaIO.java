/**
 * 
 */
package path.wiser.mobile.util;

import java.io.File;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;

/**
 * @author andrewnisbet
 * 
 */
public abstract class MediaIO extends Activity
{
	protected boolean	externalStorageAvailable;
	protected boolean	externalStorageWriteable;

	/**
	 * Method to check whether external media available and writable. This is adapted from
	 * http://developer.android.com/guide/topics/data/data-storage.html#filesExternal
	 */
	public MediaIO( String TAG )
	{
		externalStorageAvailable = false;
		externalStorageWriteable = false;
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals( state ))
		{
			// Can read and write the media
			externalStorageAvailable = externalStorageWriteable = true;
		}
		else
			if (Environment.MEDIA_MOUNTED_READ_ONLY.equals( state ))
			{
				// Can only read the media
				externalStorageAvailable = true;
				externalStorageWriteable = false;
			}
			else
			{
				// Can't read or write
				externalStorageAvailable = externalStorageWriteable = false;
			}
		Log.i( TAG, "\n\nExternal Media: readable=" + externalStorageAvailable + " writable=" + externalStorageWriteable );
	}

	/**
	 * @param path on device.
	 * @param fileName name of the file to save as.
	 * @return the path of the requested object type.
	 */
	protected File getPath( String path, String fileName )
	{
		File storage = null;
		// TODO add selector to SettingsActivity to determine if the internal or external storage should be used.
		if (isExternalStorageAvailable())
		{
			storage = Environment.getExternalStorageDirectory();
		}
		else
		{
			storage = getFilesDir(); // place files under the application's directory.
		}

		File dir = new File( storage.getAbsolutePath() + path );

		if (dir.exists() == false)
		{
			dir.mkdirs();
		}

		return new File( dir, fileName );
	}

	/**
	 * @param path
	 * @param fileName
	 * @return true if the file was deleted and false otherwise.
	 */
	public boolean removeFile( String path, String fileName )
	{
		File file = getPath( path, fileName );
		if (file.exists())
		{
			return file.delete();
		}
		return false;
	}

	/**
	 * @return the externalStorageAvailable
	 */
	public boolean isExternalStorageAvailable()
	{
		return externalStorageAvailable;
	}

	/**
	 * @return the externalStorageWriteable
	 */
	public boolean isExternalStorageWriteable()
	{
		return externalStorageWriteable;
	}
}
