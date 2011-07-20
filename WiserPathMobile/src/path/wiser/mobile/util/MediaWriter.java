/**
 * 
 */
package path.wiser.mobile.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;

/**
 * @author andrewnisbet
 * 
 */
public class MediaWriter extends Activity
{

	private static final String	TAG			= "MediaWriter";
	private static final int	BUFFER_SIZE	= 8192;
	boolean						externalStorageAvailable;
	boolean						externalStorageWriteable;

	/**
	 * Method to check whether external media available and writable. This is adapted from
	 * http://developer.android.com/guide/topics/data/data-storage.html#filesExternal
	 */
	public MediaWriter()
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
	 * @param path under the application directory
	 * @param fileName name of the file to write to in that directory; any existing
	 *        file in that directory will be clobbered.
	 * @return True if the file was written successfully and false otherwise.
	 * 
	 */
	public boolean writeFile( String path, String fileName, String data )
	{
		// Find the root of the external storage.
		// See http://developer.android.com/guide/topics/data/data-storage.html#filesExternal
		// See http://stackoverflow.com/questions/3551821/android-write-to-sd-card-folder
		File file = getPath( path, fileName );
		try
		{
			FileOutputStream fOut = new FileOutputStream( file );
			PrintWriter pw = new PrintWriter( fOut );
			pw.println( data );
			pw.flush();
			pw.close();
			fOut.close();
		}
		catch (FileNotFoundException e)
		{
			Log.e( TAG, "File not found. Did you have WRITE_EXTERNAL_STORAGE permission?" );
			return false;
		}
		catch (IOException e)
		{
			Log.e( TAG, "IO error writing file: " + file.getAbsolutePath() );
			return false;
		}
		Log.i( TAG, "Data written to " + file );
		return true;
	}

	/**
	 * This method will read the file if it is there. Files are expected to be found under the application's
	 * directory.
	 * 
	 * @param path under the application directory: /data/android/path.wiser.mobile/trace
	 * @param fileName the name of the file to read from the directory.
	 * @return The content of the file as a String.
	 */
	public String readFile( String path, String fileName )
	{
		File filePath = getPath( path, fileName );
		Log.i( TAG, "Data read from :" + filePath.getAbsolutePath() );
		if (filePath.exists() == false || filePath.length() < 1)
		{
			return "";
		}

		InputStream is = null;

		try
		{
			is = new FileInputStream( filePath );
		}
		catch (FileNotFoundException e)
		{
			Log.e( TAG, filePath.getAbsolutePath() + " file was not found, are manifest permissions set?" );
			return "";
		}

		BufferedReader br = new BufferedReader( new InputStreamReader( is ), BUFFER_SIZE ); // 2nd arg is buffer size
		StringBuffer fileString = new StringBuffer();
		String text = null;
		try
		{
			while (true)
			{
				text = br.readLine();
				// readLine() returns null if no more lines in the file
				if (text == null)
				{
					break;
				}
				fileString.append( text );
			}
			is.close();
			br.close();
		}
		catch (IOException e)
		{
			Log.e( TAG, filePath.getAbsolutePath() + " IO error reading file." );
			return "";
		}

		return fileString.toString();
	}

	/**
	 * @param path on device.
	 * @param fileName name of the file to save as.
	 * @return the path of the requested object type.
	 */
	private File getPath( String path, String fileName )
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
		File file = new File( path, fileName );
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
