/**
 * 
 */
package path.wiser.mobile.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import android.util.Log;

/**
 * @author andrewnisbet
 * 
 */
public class MediaWriter extends MediaIO
{

	private static final String	TAG	= "MediaWriter";

	/**
	 * Method to check whether external media available and writable. This is adapted from
	 * http://developer.android.com/guide/topics/data/data-storage.html#filesExternal
	 */
	public MediaWriter()
	{
		super( TAG );
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

}
