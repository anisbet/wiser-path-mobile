/**
 * 
 */
package path.wiser.mobile.geo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @author anisbet
 * 
 */
public class WiserDatabase
{
	protected static String	TAG					= "WiserDatabase";
	protected static String	DATABASE_NAME		= "books";
	protected static String	DATABASE_TABLE		= "titles";
	protected static int	DATABASE_VERSION	= 1;
	private static String	DATABASE_CREATE		= "create table titles (_id integer primary key autoincrement, "
													+ "isbn text not null, title text not null, "
													+ "publisher text not null);";

	/**
	 * @author anisbet
	 * 
	 */
	protected static class DatabaseHelper extends SQLiteOpenHelper
	{
		DatabaseHelper( Context context )
		{
			super( context, DATABASE_NAME, null, DATABASE_VERSION );
		}

		@Override
		public void onCreate( SQLiteDatabase db )
		{
			db.execSQL( DATABASE_CREATE );
		}

		@Override
		public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion )
		{
			Log.w( TAG, "Upgrading database from version " + oldVersion
				+ " to " + newVersion + ", which will destroy all old data" );
			db.execSQL( "DROP TABLE IF EXISTS titles" );
			onCreate( db );
		}
	}
}
