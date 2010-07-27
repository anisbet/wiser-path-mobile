package path.wiser.mobile.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class WiserDatabase
{
	private final static String	TAG					= "WiserDatabase";
	private final static String	DATABASE_NAME		= "books";
	private final static String	DATABASE_TABLE		= "titles";
	private final static int	DATABASE_VERSION	= 1;
	private final static String	DATABASE_CREATE		= "create table titles (_id integer primary key autoincrement, "
														+ "isbn text not null, title text not null, "
														+ "publisher text not null);";
	public static final String	KEY_ROWID			= "_id";
	public static final String	KEY_ISBN			= "isbn";
	public static final String	KEY_TITLE			= "title";
	public static final String	KEY_PUBLISHER		= "publisher";

	private Context				context				= null;
	private DatabaseHelper		DBHelper			= null;
	private SQLiteDatabase		db					= null;

	/**
	 * @param context
	 */
	public WiserDatabase( Context context )
	{
		this.context = context;
		DBHelper = new DatabaseHelper( this.context );
	}

	// ---opens the database---
	/**
	 * @return
	 * @throws SQLException
	 */
	public WiserDatabase open() throws SQLException
	{
		db = DBHelper.getWritableDatabase();
		return this;
	}

	// ---closes the database---
	/**
	 * 
	 */
	public void close()
	{
		DBHelper.close();
	}

	// ---insert a title into the database---
	/**
	 * @param isbn
	 * @param title
	 * @param publisher
	 * @return
	 */
	public long insertTitle( String isbn, String title, String publisher )
	{
		ContentValues initialValues = new ContentValues();
		initialValues.put( KEY_ISBN, isbn );
		initialValues.put( KEY_TITLE, title );
		initialValues.put( KEY_PUBLISHER, publisher );
		return db.insert( DATABASE_TABLE, null, initialValues );
	}

	// ---deletes a particular title---
	/**
	 * @param rowId
	 * @return
	 */
	public boolean deleteTitle( long rowId )
	{
		return db.delete( DATABASE_TABLE, KEY_ROWID + "=" + rowId, null ) > 0;
	}

	// ---retrieves all the titles---
	/**
	 * @return
	 */
	public Cursor getAllTitles()
	{
		return db.query( DATABASE_TABLE, new String[]
		{ KEY_ROWID, KEY_ISBN, KEY_TITLE, KEY_PUBLISHER }, null, null, null,
			null, null );
	}

	// ---retrieves a particular title---
	/**
	 * @param rowId
	 * @return
	 * @throws SQLException
	 */
	public Cursor getTitle( long rowId ) throws SQLException
	{
		Cursor mCursor = db.query( true, DATABASE_TABLE, new String[]
		{ KEY_ROWID, KEY_ISBN, KEY_TITLE, KEY_PUBLISHER }, KEY_ROWID + "="
			+ rowId, null, null, null, null, null );
		if (mCursor != null)
		{
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	/**
	 * @param rowId
	 * @param isbn
	 * @param title
	 * @param publisher
	 * @return
	 */
	public boolean updateTitle( long rowId, String isbn, String title,
		String publisher )
	{
		ContentValues args = new ContentValues();
		args.put( KEY_ISBN, isbn );
		args.put( KEY_TITLE, title );
		args.put( KEY_PUBLISHER, publisher );
		return db.update( DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null ) > 0;
	}

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
