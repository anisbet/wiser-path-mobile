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
	public final static String		TABLE_NAME			= "PoiIncident";

	public final static String[]	FIELDS				=
														{ "p_id", "title", "blog", "image", "isIncident" };

	// POI_INCIDENT_TABLE Fields
	public final static String		PI_ID				= "p_id";
	public final static String		PI_TITLE			= "title";
	public final static String		PI_BLOG				= "blog";
	public final static String		PI_IMAGE			= "image";
	public final static String		PI_IS_INCIDENT		= "isIncident";
	// POI table creation string.
	private final static String		POI_TABLE_CREATE	= "create table " + TABLE_NAME + " (" + PI_ID + " integer primary key autoincrement, "
															+ PI_TITLE + " text not null," + PI_BLOG + " text, " + PI_IMAGE + " blob,"
															+ PI_IS_INCIDENT + " integer); ";

	private ContentValues			contentValues		= null;

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
		return POI_TABLE_CREATE;
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
		contentValues.put( PI_TITLE, title );
	}

	/**
	 * @return the piBlog
	 */
	public void setBlog( String blog )
	{
		contentValues.put( PI_BLOG, blog );
	}

	/**
	 * @return the piImage
	 */
	public void setImage( byte[] img )
	{
		contentValues.put( PI_IMAGE, img );
	}

	/**
	 * Sets the IS_INCIDENT flag for this record.
	 */
	public void isIncident( Boolean isIncident )
	{
		if (isIncident)
		{
			contentValues.put( PI_IS_INCIDENT, 1 );
		}
		contentValues.put( PI_IS_INCIDENT, 0 );
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
