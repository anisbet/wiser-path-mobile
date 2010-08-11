/**
 * 
 */
package path.wiser.mobile.db;

import path.wiser.mobile.ui.WiserActivity;
import android.database.Cursor;

/**
 * @author andrewnisbet
 * 
 */
public abstract class Queryable extends WiserActivity
{
	protected Cursor	cursor	= null;

	public Queryable( String tag )
	{
		super( tag );
		// TODO Get a database object with context.
	}

	/**
	 * Selects the previous Activity for display if any.
	 */
	protected abstract void previous();

	/**
	 * Uploads the Activity, deletes the current activity on success and if any.
	 */
	protected abstract void upload();

	/**
	 * Deletes the current activity record if any. If none a new activity
	 * is created.
	 */
	protected abstract void delete();

	/**
	 * Saves the current activity.
	 */
	protected abstract void save();

	/**
	 * Selects the next Activity for display if any.
	 */
	protected abstract void next();

}
