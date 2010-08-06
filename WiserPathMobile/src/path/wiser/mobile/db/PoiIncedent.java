/**
 * 
 */
package path.wiser.mobile.db;

/**
 * @author andrewnisbet
 * 
 */
public class PoiIncedent implements WiserDatabaseTable
{
	public final static String	TABLE_NAME			= "PoiIncident";

	// POI_INCIDENT_TABLE Fields
	public final static String	PI_ID				= "p_id";
	public final static String	PI_TITLE			= "title";
	public final static String	PI_BLOG				= "blog";
	public final static String	PI_IMAGE			= "image";
	public final static String	PI_IS_INCIDENT		= "isIncident";
	// POI table creation string.
	private final static String	POI_TABLE_CREATE	= "create table " + TABLE_NAME + " (" + PI_ID + " integer primary key autoincrement, " + PI_TITLE
														+ " text not null," + PI_BLOG + " text, " + PI_IMAGE + " blob," + PI_IS_INCIDENT
														+ " integer); ";

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return POI_TABLE_CREATE;
	}

}
