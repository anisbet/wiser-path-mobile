/**
 * 
 */
package path.wiser.mobile.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import android.util.Log;

/**
 * @author andrewnisbet
 * 
 */
public class MediaReader extends MediaIO
{
	private static final String	TAG			= "MediaReader";
	private static final int	BUFFER_SIZE	= 8192;
	private String				fileName	= "";

	/**
	 * Method to check whether external media available and writable. This is adapted from
	 * http://developer.android.com/guide/topics/data/data-storage.html#filesExternal
	 */
	public MediaReader()
	{
		super( TAG );
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
		// get an input stream to read from.
		this.fileName = fileName;
		FileInputStream is = getInputStream( path, this.fileName );
		// File filePath = getPath( path, fileName );
		// Log.i( TAG, "Data read from :" + filePath.getAbsolutePath() );
		// if (filePath.exists() == false || filePath.length() < 1)
		// {
		// return "";
		// }
		//
		// InputStream is = null;
		//
		// try
		// {
		// is = new FileInputStream( filePath );
		// }
		// catch (FileNotFoundException e)
		// {
		// Log.e( TAG, filePath.getAbsolutePath() + " file was not found, are manifest permissions set?" );
		// return "";
		// }

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
			Log.e( TAG, this.fileName + " IO error reading file." );
			return "";
		}

		return fileString.toString();
	}
}
