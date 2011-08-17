/**
 * 
 */
package path.wiser.mobile.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import path.wiser.mobile.WPEnvironment;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
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
	public boolean writeTextFile( String path, String fileName, String data )
	{
		// Find the root of the external storage.
		// See http://developer.android.com/guide/topics/data/data-storage.html#filesExternal
		// See http://stackoverflow.com/questions/3551821/android-write-to-sd-card-folder

		// get an input stream to read from.
		// this.fileName = fileName;
		File dir = Environment.getExternalStorageDirectory();
		File file = new File( dir, path + "/" + fileName );

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
	 * Writes a BMP image to the user's preferred location if possible.
	 * 
	 * @param path under the available directory -- either the SD card or the internal App directory.
	 * @param fileName name of the image to write.
	 * @param image BMP to write to file.
	 * @return true if the operation was a success and false otherwise.
	 */
	public boolean writeImageFile( String path, String fileName, Bitmap image )
	{
		File dir = Environment.getExternalStorageDirectory();
		File file = new File( dir, path + "/" + fileName );

		FileOutputStream fileOutputStream;
		try
		{
			fileOutputStream = new FileOutputStream( file.toString() );
			BufferedOutputStream bufferedOutputStream = new BufferedOutputStream( fileOutputStream );
			image.compress( CompressFormat.JPEG, WPEnvironment.DEFAULT_IMAGE_QUALITY, bufferedOutputStream );
			bufferedOutputStream.flush();
			bufferedOutputStream.close();
		}
		catch (Exception e)
		{
			Log.e( TAG, "Error occured while writing image." );
			return false;
		}

		// Read more: http://www.brighthub.com/mobile/google-android/articles/64048.aspx#ixzz1VJ7dtwEV
		return true;

	}
}
