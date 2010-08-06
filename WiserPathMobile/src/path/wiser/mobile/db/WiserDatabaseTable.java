/**
 * 
 */
package path.wiser.mobile.db;

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
	public abstract String toString();
}
