package path.wiser.mobile.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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
	public final static String			TAG						= "WiserDB";
	public final static String			DATABASE_NAME			= "WiserPathDatabase";
	public final static int				DATABASE_VERSION		= 1;

	public static WiserDatabaseTable	poiTable				= null;
	public static WiserDatabaseTable	traceTable				= null;

	// TraceLocationTable
	public final static String			TRACE_LOCATION_TABLE	= "TraceLocationTable";

	// TracePoiIdTable
	public final static String			TRACE_POI_ID_TABLE		= "TracePoiIdTable";

	// TagsTable
	public final static String			TAGS_TABLE				= "TagsTable";

	// public final static String DATABASE_CREATE =
	// "create table titles (_id integer primary key autoincrement, "
	// + "isbn text not null, title text not null, " +
	// "publisher text not null);";

	private Context						context					= null;
	private DatabaseHelper				DBHelper				= null;
	protected SQLiteDatabase			db						= null;

	/**
	 * @param context
	 */
	public WiserDatabase( Context context )
	{
		this.context = context;
		this.poiTable = new PoiIncedent();
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
		// TODO serialize database to memory
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

	/**
	 * @param wdbt table with row you wish to save.
	 * @return
	 */
	public long insert( WiserDatabaseTable wdbt )
	{
		// TODO remove this line when finished testing.
		if (db == null) System.out.println( "What the hell the db is null!!" );
		// System.out.println( wdbt.getName() + ":" + wdbt.getContentValues() );
		return db.insert( wdbt.getName(), null, wdbt.getContentValues() );
	}

	/**
	 * @param q
	 * @return True if at least one item matching the query deleted and false
	 *         otherwise.
	 */
	public boolean delete( WiserQuery q )
	{
		// return db.delete( POI_INCIDENT_TABLE, KEY_ROWID + "=" + rowId, null )
		// >
		// 0;
		return db.delete( q.getTable(), q.getWhereClause(), q.getWhereArgs() ) > 0;
	}

	/**
	 * @return Cursor of results.
	 */
	public Cursor query( WiserQuery q )
	{
		Cursor c = null;
		if (q.getLimit().compareTo( "null" ) != 0)
			c = db.query( q.isDistinct(), q.getTable(), q.getColumns(), q.getWhereClause(), q.getWhereArgs(), q.getGroupBy(), q.getHaving(),
				q.getOrderBy(), q.getLimit() );
		else
			c = db.query( q.getTable(), q.getColumns(), q.getWhereClause(), q.getWhereArgs(), q.getGroupBy(), q.getHaving(), q.getOrderBy() );
		if (c != null)
		{
			c.moveToFirst();
			System.out.println( "cursor count: " + c.getCount() );
		}

		return c;
	}

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
		DatabaseHelper( Context context )
		{
			super( context, DATABASE_NAME, null, DATABASE_VERSION );
		}

		@Override
		public void onCreate( SQLiteDatabase db )
		{
			System.out.println( "create string: " + poiTable.create() );
			db.execSQL( poiTable.create() );
		}

		@Override
		public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion )
		{
			Log.w( TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data" );
			// TODO we should allow for the uploading of data, this next command
			// will
			// just drop the table.
			db.execSQL( "DROP TABLE IF EXISTS " + PoiIncedent.TABLE_NAME );
			onCreate( db );
		}
	}

}
