/**
 * 
 */
package path.wiser.mobile.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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

	public MediaWriter()
	{

	}

	public MediaWriter( String path )
	{

	}

	/**
	 * Method to check whether external media available and writable. This is adapted from
	 * http://developer.android.com/guide/topics/data/data-storage.html#filesExternal
	 */
	private void checkExternalMedia()
	{
		boolean mExternalStorageAvailable = false;
		boolean mExternalStorageWriteable = false;
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals( state ))
		{
			// Can read and write the media
			mExternalStorageAvailable = mExternalStorageWriteable = true;
		}
		else
			if (Environment.MEDIA_MOUNTED_READ_ONLY.equals( state ))
			{
				// Can only read the media
				mExternalStorageAvailable = true;
				mExternalStorageWriteable = false;
			}
			else
			{
				// Can't read or write
				mExternalStorageAvailable = mExternalStorageWriteable = false;
			}
		Log.i( TAG, "\n\nExternal Media: readable=" + mExternalStorageAvailable + " writable=" + mExternalStorageWriteable );
	}

	/**
	 * Method to write ascii text characters to file on SD card. Note that you must add a
	 * WRITE_EXTERNAL_STORAGE permission to the manifest file or this method will throw
	 * a FileNotFound Exception because you won't have write permission.
	 */

	/**
	 * 
	 */
	public void writeToSDFile( String data )
	{
		// Find the root of the external storage.
		// See http://developer.android.com/guide/topics/data/data-storage.html#filesExternal
		File root = android.os.Environment.getExternalStorageDirectory();

		// See http://stackoverflow.com/questions/3551821/android-write-to-sd-card-folder

		File dir = new File( root.getAbsolutePath() + "/download" );
		dir.mkdirs();
		File file = new File( dir, "myData.txt" );

		try
		{
			FileOutputStream f = new FileOutputStream( file );
			PrintWriter pw = new PrintWriter( f );
			pw.println( "Howdy do to you." );
			pw.println( "Here is a second line." );
			pw.println( data );
			pw.flush();
			pw.close();
			f.close();
		}
		catch (FileNotFoundException e)
		{
			Log.e( TAG, "File not found. Did you have WRITE_EXTERNAL_STORAGE permission?" );
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		Log.i( TAG, "\n\nFile written to " + file );
	}

	/**
	 * Method to read in a text file placed in the res/raw directory of the application. The
	 * method reads in all lines of the file sequentially.
	 */

	/**
	 * @return
	 */
	public String readRaw()
	{
		// tv.append( "\nData read from res/raw/textfile.txt:" );
		// InputStream is = this.getResources().openRawResource( R.raw.textfile );
		// FileOutputStream fOut = openFileOutput( "samplefile.txt", MODE_WORLD_READABLE );
		// InputStreamReader isr = new InputStreamReader( is );
		// BufferedReader br = new BufferedReader( isr, BUFFER_SIZE ); // 2nd arg is buffer size
		//
		// // More efficient (less readable) implementation of above is the composite expression
		// /*
		// * BufferedReader br = new BufferedReader(new InputStreamReader(
		// * this.getResources().openRawResource(R.raw.textfile)), 8192);
		// */
		// StringBuffer fileString = new StringBuffer();
		// String text = null;
		// try
		// {
		// while (true)
		// {
		// text = br.readLine();
		// // readLine() returns null if no more lines in the file
		// if (text == null)
		// {
		// break;
		// }
		// fileString.append( text );
		// }
		// isr.close();
		// is.close();
		// br.close();
		// }
		// catch (IOException e)
		// {
		// e.printStackTrace();
		// }
		//
		// return fileString.toString();
		return "";
	}
}
