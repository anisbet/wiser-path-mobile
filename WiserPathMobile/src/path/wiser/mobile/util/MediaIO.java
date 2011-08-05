/**
 * 
 */
package path.wiser.mobile.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import path.wiser.mobile.WPEnvironment;
import android.app.Activity;
import android.os.Environment;
import android.util.Log;

/**
 * @author andrewnisbet
 * 
 */
public abstract class MediaIO extends Activity
{
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
			// storage = getFilesDir(); // place files under the application's directory. // TODO fix me
			// storage = getDir( "data", MODE_WORLD_WRITEABLE ); // place files under the application's 'data'
			// directory.
			try
			{
				FileOutputStream fw = this.openFileOutput( fileName, MODE_WORLD_READABLE );
			}
			catch (FileNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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

	/**
	 * @param path
	 * @param fileName
	 * @return
	 */
	protected FileInputStream getInputStream( String path, String fileName )
	{
		// first off find out if there is external storage and if that is the preference for the user.
		if (deviceHasWritableExternalMedia() && WPEnvironment.isPreferExternalStorage())
		{
			File storage = Environment.getExternalStorageDirectory();

			File dir = new File( storage.getAbsolutePath() + path );

			Log.i( TAG, "Data read from :" + dir.getAbsolutePath() );

			try
			{
				return new FileInputStream( dir );
			}
			catch (FileNotFoundException e)
			{
				Log.e( TAG, dir.getAbsolutePath() + " file was not found, are manifest permissions set?" );
			}
		}
		else
		{
			try
			{
				return openFileInput( fileName );
			}
			catch (FileNotFoundException e)
			{
				Log.e( TAG, "can't read the file " + fileName );
			}
		}
		return null;
	}
}
