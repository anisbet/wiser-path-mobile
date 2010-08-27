/**
 * 
 */
package path.wiser.mobile.db;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

/**
 * Helper class to encapsulate the constants and a single record of a database
 * table.
 * 
 * @author anisbet
 * 
 */
public class PoiRelation extends RelationHelper
{

	protected static String				tableName		= "PoiTable";

	public final static int				COLUMN_SIZE		= 2;
	// public final static int COLUMN_SIZE = 5;
	// POI_INCIDENT_TABLE Fields
	public static final int				ID				= 0;
	public static final int				TITLE			= 1;
	public static final int				BLOG			= 2;
	public static final int				IMAGE			= 3;
	public static final int				IS_INCIDENT		= 4;

	protected final static String[]		columns			=
														{ "id", "title", "blog", "image", "isIncident" };
	protected final static String		createStatement	= "create table " + tableName + " (" + columns[ID] + " INTEGER PRIMARY KEY, "
															+ columns[TITLE] + " text ); ";
	protected final static String		insertString	= "insert into " + tableName + "(name) values (?)";
	protected static SQLiteStatement	insertStatement	= null;

	/**
	 * Creates a PoiRelation object.
	 */
	public PoiRelation( SQLiteDatabase db )
	{
		// for some relations this can happen frequently so good to have a
		// pre-compiled version around.
		PoiRelation.insertStatement = db.compileStatement( insertString );
	}

	public String createTable()
	{
		return PoiRelation.createStatement;
	}

	public String getName()
	{
		return PoiRelation.tableName;
	}

	public SQLiteStatement getPrecompiledInsertStatement()
	{
		return PoiRelation.insertStatement;
	}

	public void insert( String title2, String blog2, byte[] image2, String tags )
	{
		insertStatement.bindString( TITLE, title2 );
		insertStatement.bindString( BLOG, blog2 );
		insertStatement.bindBlob( IMAGE, image2 );
		insertStatement.bindLong( IS_INCIDENT, 0L );
	}

	// /**
	// * Inserts a record into the database.
	// *
	// * @param title
	// * @param blog
	// * @param image
	// * @param isIncident
	// * @return
	// */
	// public static boolean insertRow( String title, String blog, byte[] image,
	// boolean isIncident )
	// {
	// return true;
	// }
}
