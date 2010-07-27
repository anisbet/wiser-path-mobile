package path.wiser.mobile.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class WiserDatabase
{
	public final static String	TAG						= "WiserDB";
	public final static String	DATABASE_NAME			= "WiserPathDatabase";
	public final static int		DATABASE_VERSION		= 1;

	// tables in the database.
	public final static String	POI_INCIDENT_TABLE		= "PoiTable";
	public final static String	TRACE_TABLE				= "TraceTable";
	public final static String	TRACE_LOCATION_TABLE	= "TraceLocationTable";
	public final static String	POI_LOCATION_TABLE		= "PoiLocationTable";
	public final static String	TRACE_POI_ID_TABLE		= "TracePoiIdTable";
	public final static String	TAGS_TABLE				= "TagsTable";

	public final static String	DATABASE_CREATE			= "create table titles (_id integer primary key autoincrement, "
															+ "isbn text not null, title text not null, "
															+ "publisher text not null);";

	// POI_INCIDENT_TABLE Record
	public final static String	PI_ID					= "_id";
	public final static String	PI_TITLE				= "title";
	public final static String	PI_BLOG					= "blog";
	public final static String	PI_IMAGE				= "image";
	public final static String	PI_IS_INCIDENT			= "isIncident";

	// public final static String TRACE_TABLE = "TraceTable";
	// public final static String TRACE_LOCATION_TABLE = "TraceLocationTable";
	// public final static String POI_LOCATION_TABLE = "PoiLocationTable";
	// public final static String TRACE_POI_ID_TABLE = "TracePoiIdTable";
	// public final static String TAGS_TABLE = "TagsTable";

	public static final String	KEY_ROWID				= "_id";
	public static final String	KEY_ISBN				= "isbn";
	public static final String	KEY_TITLE				= "title";
	public static final String	KEY_PUBLISHER			= "publisher";

	private Context				context					= null;
	private DatabaseHelper		DBHelper				= null;
	private SQLiteDatabase		db						= null;

	/**
	 * @param context
	 */
	public WiserDatabase( Context context )
	{
		this.context = context;
		DBHelper = new DatabaseHelper( this.context );
	}

	/**
	 * @return WiserDatabase
	 * @throws SQLException
	 */
	public WiserDatabase open() throws SQLException
	{
		db = DBHelper.getWritableDatabase();
		return this;
	}

	/**
	 * Closes the database.
	 */
	public void close()
	{
		DBHelper.close();
	}

	// /**
	// * @param isbn
	// * @param title
	// * @param publisher
	// * @return
	// */
	// public long insertTitle( String isbn, String title, String publisher )
	// {
	// ContentValues initialValues = new ContentValues();
	// initialValues.put( KEY_ISBN, isbn );
	// initialValues.put( KEY_TITLE, title );
	// initialValues.put( KEY_PUBLISHER, publisher );
	// return db.insert( POI_INCIDENT_TABLE, null, initialValues );
	// }
	//
	// // ---deletes a particular title---
	// /**
	// * @param rowId
	// * @return
	// */
	// public boolean deleteTitle( long rowId )
	// {
	// return db.delete( POI_INCIDENT_TABLE, KEY_ROWID + "=" + rowId, null ) >
	// 0;
	// }
	//
	// // ---retrieves all the titles---
	// /**
	// * @return
	// */
	// public Cursor getAllTitles()
	// {
	// return db.query( POI_INCIDENT_TABLE, new String[]
	// { KEY_ROWID, KEY_ISBN, KEY_TITLE, KEY_PUBLISHER }, null, null, null,
	// null, null );
	// }
	//
	// // ---retrieves a particular title---
	// /**
	// * @param rowId
	// * @return
	// * @throws SQLException
	// */
	// public Cursor getTitle( long rowId ) throws SQLException
	// {
	// Cursor mCursor = db.query( true, POI_INCIDENT_TABLE, new String[]
	// { KEY_ROWID, KEY_ISBN, KEY_TITLE, KEY_PUBLISHER }, KEY_ROWID + "="
	// + rowId, null, null, null, null, null );
	// if (mCursor != null)
	// {
	// mCursor.moveToFirst();
	// }
	// return mCursor;
	// }
	//
	// /**
	// * @param rowId
	// * @param isbn
	// * @param title
	// * @param publisher
	// * @return
	// */
	// public boolean updateTitle( long rowId, String isbn, String title,
	// String publisher )
	// {
	// ContentValues args = new ContentValues();
	// args.put( KEY_ISBN, isbn );
	// args.put( KEY_TITLE, title );
	// args.put( KEY_PUBLISHER, publisher );
	// return db.update( POI_INCIDENT_TABLE, args, KEY_ROWID + "=" + rowId, null
	// ) >
	// 0;
	// }

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
