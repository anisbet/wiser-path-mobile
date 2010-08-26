/**
 * 
 */
package path.wiser.mobile.db;

/**
 * Helper class to encapsulate the constants and a single record of a database
 * table.
 * 
 * @author anisbet
 * 
 */
public class PoiRelation extends RelationHelper
{

	public final static String[]	COLUMNS		=
												{ "title", "blog", "image", "isIncident" };
	// POI_INCIDENT_TABLE Fields
	private static final int		TITLE		= 0;
	private static final int		BLOG		= 1;
	private static final int		IMAGE		= 2;
	private static final int		IS_INCIDENT	= 3;

	/**
	 * Creates a PoiRelation object.
	 */
	public PoiRelation()
	{
		super( "PoiTable" );
		RelationHelper.createStatement = "create table " + tableName + " (id INTEGER PRIMARY KEY, " + COLUMNS[TITLE] + " text not null,"
			+ COLUMNS[BLOG] + " text, " + COLUMNS[IMAGE] + " blob," + COLUMNS[IS_INCIDENT] + " integer); ";
	}

}
