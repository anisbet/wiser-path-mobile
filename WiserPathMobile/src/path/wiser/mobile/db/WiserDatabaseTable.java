/**
 * 
 */
package path.wiser.mobile.db;

import android.content.ContentValues;

/**
 * A {@link WiserDatabaseTable} is an object that encapsulates all the rows of a
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
public interface WiserDatabaseTable
{
	/**
	 * @return String SQLite CREATE statement for this table.
	 */
	public abstract String create();

	/**
	 * @return String name of the table.
	 */
	public abstract String getName();

	/**
	 * @return the field and value pairs for a record in a table.
	 */
	public abstract ContentValues getContentValues();

	/**
	 * Creates a new record, wipes the old one.
	 */
	public abstract void newRecord();
}
