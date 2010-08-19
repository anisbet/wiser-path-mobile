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

	public static final String		TABLE_NAME		= "TraceTable";
	public final static String[]	COLUMNS			=
													{ "title", "blog" };
	private final static int		TITLE			= 0;
	private final static int		BLOG			= 1;

	// POI table creation string.
	public final static String		CREATE			= "create table " + TABLE_NAME + " (" + COLUMNS[TITLE] + " text not null," + COLUMNS[BLOG]
														+ " text); ";
	// TODO add fields for lat long and time.
	private ContentValues			contentValues	= null;

	/**
	 * 
	 */
	public Trace()
	{
		super();
		this.contentValues = new ContentValues();
	}

	@Override
	public String create()
	{
		return CREATE;
	}

	@Override
	public String getName()
	{
		return TABLE_NAME;
	}

	@Override
	public ContentValues getContentValues()
	{
		return this.contentValues;
	}

	@Override
	public void newRecord()
	{
		this.contentValues = new ContentValues();
	}

	/**
	 * @return the piTitle
	 */
	public void setTitle( String title )
	{
		contentValues.put( COLUMNS[TITLE], title );
	}

	public String getTitle()
	{
		return contentValues.getAsString( COLUMNS[TITLE] );
	}

	/**
	 * @return the piBlog
	 */
	public void setBlog( String blog )
	{
		contentValues.put( COLUMNS[BLOG], blog );
	}

	public String getBlog()
	{
		return contentValues.getAsString( COLUMNS[BLOG] );
	}

}
