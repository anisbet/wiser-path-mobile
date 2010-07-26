/**
 * 
 */
package path.wiser.mobile.ui;

import path.wiser.mobile.R;
import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author anisbet
 *         This class provides customized toast messages without the need to do
 *         a lot of coding. Extend this class if you need to alert the user of
 *         something important.
 * 
 */
public abstract class WiserActivity extends Activity
{
	/**
	 * Creates a customized Toast with the wiser logo.
	 * 
	 * @param message
	 */
	protected void showMessage( String message )
	{
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate( R.layout.custom_toast_layout,
			(ViewGroup) findViewById( R.id.custom_toast_layout_root ) );

		ImageView image = (ImageView) layout.findViewById( R.id.image );
		image.setImageResource( R.drawable.icon );
		TextView text = (TextView) layout.findViewById( R.id.text );
		text.setText( message );

		Toast toast = new Toast( getApplicationContext() );
		toast.setGravity( Gravity.CENTER_VERTICAL, 0, 0 );
		toast.setDuration( Toast.LENGTH_LONG );
		toast.setView( layout );
		toast.show();
	}

	/**
	 * Selects the previous Activity for display if any.
	 */
	abstract protected void previous();

	/**
	 * Uploads the Activity, deletes the current activity on success and if any.
	 */
	abstract protected void upload();

	/**
	 * Deletes the current activity Activity if any. If none a new activity
	 * is created.
	 */
	abstract protected void delete();

	/**
	 * Saves the current activity.
	 */
	abstract protected void save();

	/**
	 * Selects the next Activity for display if any.
	 */
	abstract protected void next();

	/**
	 * Convieniece class that helps improve readability of the code.
	 */
	protected class ClearTextView implements OnClickListener
	{

		@Override
		public void onClick( View v )
		{
			( (TextView) v ).setText( "" );

		}

	}

}
