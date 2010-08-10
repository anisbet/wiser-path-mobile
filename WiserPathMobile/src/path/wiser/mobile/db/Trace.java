/**
 * 
 */
package path.wiser.mobile.db;

import android.content.ContentValues;

/**
 * @author andrewnisbet
 * 
 */
public class Trace implements WiserDatabaseTable
{

	public static final String		TABLE_NAME		= "Trace";
	public final static String[]	COLUMNS			=
													{ "t_id", "title", "blog" };
	private final static int		ID				= 0;
	private final static int		TITLE			= 1;
	private final static int		BLOG			= 2;

	// POI table creation string.
	public final static String		CREATE			= "create table " + TABLE_NAME + " (" + COLUMNS[ID] + " integer primary key autoincrement, "
														+ COLUMNS[TITLE] + " text not null," + COLUMNS[BLOG] + " text); ";
	// TODO add fields for lat long and time.
	private ContentValues			contentValues	= null;

	/**
	 * 
	 */
	public Trace()
	{
		// TODO Auto-generated constructor stub
	}

	@Override
	public String create()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContentValues getContentValues()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void newRecord()
	{
		// TODO Auto-generated method stub

	}

}
