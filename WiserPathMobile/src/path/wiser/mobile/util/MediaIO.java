/**
 * 
 */
package path.wiser.mobile.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;

/**
 * @author andrewnisbet
 * 
 */
public abstract class MediaIO extends Activity
{
	// ///////////////////////////
	// Note to Developer: MediaIO, MediaWriter and MediaReader support only saving to external directories (ie the SD
	// card)
	// ///////////////////////////
	protected static boolean	externalStorageAvailable;
	protected static boolean	externalStorageWriteable;
	protected String			TAG;

	static
	{
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
	}

	/**
	 * Method to check whether external media available and writable. This is adapted from
	 * http://developer.android.com/guide/topics/data/data-storage.html#filesExternal
	 */
	public MediaIO( String TAG )
	{
		// for testing.
		this.TAG = TAG;
		Log.i( TAG, "\n\nExternal Media: readable=" + externalStorageAvailable + " writable=" + externalStorageWriteable );
	}

	protected FileInputStream getInputStream( String fileName )
	{
		return null;
	}

	protected FileOutputStream getOutputStream( String fileName )
	{
		return null;
	}

	/**
	 * Deletes the argument fileName from the argument path if possible.
	 * 
	 * @param path
	 * @param fileName
	 * @return true if the file was deleted and false otherwise.
	 */
	public boolean removeFile( String path, String fileName )
	{
		File dir = Environment.getExternalStorageDirectory();
		File file = new File( dir, path + "/" + fileName );
		if (file.exists())
		{
			return file.delete();
		}
		return false;
	}

	/**
	 * @return the externalStorageAvailable
	 */
	public static boolean isExternalStorageAvailable()
	{
		return externalStorageAvailable;
	}

	/**
	 * @return the externalStorageWriteable
	 */
	public static boolean isExternalStorageWriteable()
	{
		return externalStorageWriteable;
	}

	/**
	 * @return true if external media is installed (SD card) and false otherwise.
	 */
	public static boolean deviceHasWritableExternalMedia()
	{
		return externalStorageAvailable && externalStorageWriteable;
	}

}
