package path.wiser.mobile.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

/**
 * This is the database for all data stored on the device. All the traces and
 * images are stored in this and then it is saved to memory.
 * This handles all the query transactions to and from the database. Activity
 * specific queries can be found in their own classes.
 * 
 * @author anisbet
 * 
 */
public class WiserDatabase
{
	public enum Tables // used when we move all the relation helpers to just
						// arrays of helpers.
	{
		POI, TRACE, LOCATION, TAGS, TRACE_POI
	}

	public final static String		TAG					= "WiserDB";
	public final static String		DATABASE_NAME		= "WiserPathDatabase";
	public final static int			DATABASE_VERSION	= 1;

	private Context					context				= null;
	private DatabaseHelper			dbHelper			= null;
	protected SQLiteDatabase		db					= null;

	public final static String		TABLE_NAME			= "PoiTable";
	// private static final String INSERT = "insert into " + TABLE_NAME +
	// "(name) values (?)";
	// private SQLiteStatement insertString = null;

	protected static PoiRelation	poi					= null;

	/**
	 * @param context
	 */
	public WiserDatabase( Context context )
	{
		this.context = context;
		this.dbHelper = new DatabaseHelper( this.context );
		this.db = dbHelper.getWritableDatabase();
		poi = new PoiRelation( this.db );
	}

	public long insert( String title, String blog, byte[] image, String tags )
	{
		SQLiteStatement insertStatement = poi.getPrecompiledInsertStatement();
		insertStatement.bindString( 1, title );
		return insertStatement.executeInsert();
	}

	public void deleteAll()
	{
		this.db.delete( TABLE_NAME, null, null );
	}

	public List<String> selectAll()
	{
		List<String> list = new ArrayList<String>();
		Cursor cursor = this.db.query( TABLE_NAME, new String[]
		{ "name" }, null, null, null, null, "name" );
		if (cursor.moveToFirst())
		{
			do
			{
				list.add( cursor.getString( 0 ) );
			}
			while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed())
		{
			cursor.close();
		}
		return list;
	}

	/**
	 * Closes the database.
	 */
	public void close()
	{
		dbHelper.close();
	}

	/**
	 * A helper class to manage database creation and version management.
	 * 
	 * You create a subclass implementing onCreate(SQLiteDatabase),
	 * onUpgrade(SQLiteDatabase, int, int) and optionally
	 * onOpen(SQLiteDatabase), and this class takes care of opening the database
	 * if it exists, creating it if it does not, and upgrading it as necessary.
	 * Transactions are used to make sure the database is always in a sensible
	 * state.
	 * 
	 * This class makes it easy for ContentProvider implementations to defer
	 * opening and upgrading the database until first use, to avoid blocking
	 * application startup with long-running database upgrades.
	 * 
	 * 
	 */
	protected static class DatabaseHelper extends SQLiteOpenHelper
	{
		protected DatabaseHelper( Context context )
		{
			super( context, DATABASE_NAME, null, DATABASE_VERSION );
		}

		@Override
		public void onCreate( SQLiteDatabase db )
		{
			db.execSQL( poi.createTable() );
		}

		@Override
		public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion )
		{
			Log.w( TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data" );
			db.execSQL( "DROP TABLE IF EXISTS " + poi.getName() );
			onCreate( db );
		}
	}

}