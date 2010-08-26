/**
 * 
 */
package path.wiser.mobile.db;

/**
 * @author andrewnisbet
 * 
 */
public class TraceRelation extends RelationHelper
{

	public final static String[]	COLUMNS	=
											{ "title", "blog" };
	private final static int		TITLE	= 0;
	private final static int		BLOG	= 1;

	/**
	 * 
	 */
	public TraceRelation()
	{
		super( "TraceTable" );
		RelationHelper.createStatement = "create table " + RelationHelper.tableName + " (id INTEGER PRIMARY KEY, " + COLUMNS[TITLE]
			+ " text not null," + COLUMNS[BLOG] + " text); ";
	}

}
