/**
 * 
 */
package path.wiser.mobile.db;

import android.content.ContentValues;

/**
 * Helper class to encapsulate the constants and a single record of a database
 * table.
 * 
 * @author anisbet
 * 
 */
public class PoiIncedent implements WiserDatabaseTable
{
	public final static String		TABLE_NAME		= "PoiIncident";

	public final static String[]	COLUMNS			=
													{ "title", "blog", "image", "isIncident" };
	// POI_INCIDENT_TABLE Fields
	private static final int		TITLE			= 0;
	private static final int		BLOG			= 1;
	private static final int		IMAGE			= 2;
	private static final int		IS_INCIDENT		= 3;

	// POI table creation string.
	public final static String		CREATE			= "create table " + TABLE_NAME + " (" + COLUMNS[TITLE] + " text not null," + COLUMNS[BLOG]
														+ " text, " + COLUMNS[IMAGE] + " blob," + COLUMNS[IS_INCIDENT] + " integer); ";
	// TODO add fields for lat long and time.
	private ContentValues			contentValues	= null;

	/**
	 * Creates a PoiIncedent object.
	 */
	public PoiIncedent()
	{
		super();
		this.contentValues = new ContentValues();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String create()
	{
		return CREATE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see path.wiser.mobile.db.WiserDatabaseTable#getContentValues()
	 */
	@Override
	public ContentValues getContentValues()
	{
		return contentValues;
	}

	@Override
	public String getName()
	{
		return TABLE_NAME;
	}

	/**
	 * @return the piTitle
	 */
	public void setTitle( String title )
	{
		contentValues.put( COLUMNS[TITLE], title );
	}

	/**
	 * @return the piBlog
	 */
	public void setBlog( String blog )
	{
		contentValues.put( COLUMNS[BLOG], blog );
	}

	/**
	 * @return the piImage
	 */
	public void setImage( byte[] img )
	{
		contentValues.put( COLUMNS[IMAGE], img );
	}

	/**
	 * Sets the IS_INCIDENT flag for this record.
	 */
	public void isIncident( Boolean isIncident )
	{
		if (isIncident)
		{
			contentValues.put( COLUMNS[IS_INCIDENT], 1 );
		}
		contentValues.put( COLUMNS[IS_INCIDENT], 0 );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see path.wiser.mobile.db.WiserDatabaseTable#newRecord()
	 */
	@Override
	public void newRecord()
	{
		contentValues = new ContentValues();
	}

}
