/**
 * 
 */
package path.wiser.mobile.util;

import path.wiser.mobile.ui.WiserActivityHelper;
import android.database.Cursor;

/**
 * The Selectable object remembers which record it is displaying and how to find
 * the next one and previous one. It basically scrolls through
 * a {@link Cursor} object results set. Each time the current displayed object
 * becomes the current so it can be referenced on Activity startup.
 * 
 * This encapsulates the menu operations away from the activity screen.
 * 
 * @author anisbet
 * 
 */
public abstract class Selectable extends WiserActivityHelper
{

	private int	currentRecord;

	public Selectable( String tag )
	{
		super( tag );
	}

	/**
	 * Selects the previous Activity for display if any. This method is called
	 * when the activity first loads to load the last version of the activity if
	 * any.
	 * If there is none this method must set the activity to a new activity.
	 * @return TODO
	 */
	protected abstract boolean previous();

	/**
	 * Uploads the Activity, deletes the current activity on success and if any.
	 * @return TODO
	 */
	protected abstract boolean upload();

	/**
	 * Deletes the current activity record if any. If none a new activity
	 * is created.
	 * @return TODO
	 */
	protected abstract boolean delete();

	/**
	 * Saves the current activity.
	 * @return TODO
	 */
	protected abstract boolean save();

	/**
	 * Selects the next Activity for display if any.
	 * @return TODO
	 */
	protected abstract boolean next();

	/**
	 * @return the currentRecordId of the activity.
	 */
	public int getId()
	{
		return currentRecord;
	}

}
