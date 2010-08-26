/**
 * 
 */
package path.wiser.mobile.db;

/**
 * A {@link RelationHelper} is an object that encapsulates all the rows of a
 * table or relation. The row's names are stored in subclasses of this
 * object. This object can also act as a container of a single row of data for a
 * given table. Be sure to use the {{@link #newRecord()} method to reset the row
 * values.
 * All tables must implement this interface to operate within the
 * WiserPathDatabase. This interface ensures that the names of fields and the
 * table itself is managed in a consistent way.
 * 
 * @author anisbet
 * 
 */
public abstract class RelationHelper
{
	protected static String		tableName		= "Table";
	protected static String		createStatement	= "";
	protected static String[]	columns			= null;

	protected RelationHelper( String tableName )
	{
		RelationHelper.tableName = tableName;
	}

	/**
	 * @return String SQLite CREATE statement for this table.
	 */
	public static String createTable()
	{
		return createStatement;
	}

	/**
	 * @return String name of the table.
	 */
	public static String getName()
	{
		return tableName;
	}

	public static String[] getColumns()
	{
		return columns;
	}

}
