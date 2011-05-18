/**
 * 
 */
package path.wiser.mobile.db;

import path.wiser.mobile.ui.WiserActivityHelper;
import android.database.Cursor;

/**
 * The Queryable object remembers which record it is displaying and how to find
 * the next one and previous one. It basically scrolls through
 * a {@link Cursor} object results set. Each time the current displayed object
 * becomes the current so it can be referenced on Activity startup.
 * 
 * This encapsulates the menu operations away from the activity screen.
 * 
 * @author anisbet
 * 
 */
public abstract class Queryable extends WiserActivityHelper
{
	protected Cursor	cursor			= null;
	private long		currentRecordId	= 0L;	// This is unique to each row in
												// each table.

	public Queryable( String tag )
	{
		super( tag );
		// TODO Get a database object with context.
	}

	/**
	 * Selects the previous Activity for display if any. This method is called
	 * when the activity first loads to load the last version of the activity if
	 * any.
	 * If there is none this method must set the activity to a new activity.
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

	/**
	 * @return the currentRecordId of the activity.
	 */
	public long getId()
	{
		return currentRecordId;
	}

}
