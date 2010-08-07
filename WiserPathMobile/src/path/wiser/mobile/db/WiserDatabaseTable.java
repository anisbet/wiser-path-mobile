/**
 * 
 */
package path.wiser.mobile.db;

import android.content.ContentValues;

/**
 * All tables must implement this interface to operate within the
 * WiserPathDatabase. This interface ensures that the names of fields and the
 * table itself is managed in a consistent way.
 * 
 * @author anisbet
 * 
 */
public interface WiserDatabaseTable
{
	/**
	 * @return String of the create table sql.
	 */
	public abstract String create();

	/**
	 * @return String name of the table.
	 */
	public abstract String getName();

	/**
	 * @return the field names and value pairs for a record.
	 */
	public abstract ContentValues getContentValues();
}
