/**
 * 
 */
package path.wiser.mobile.ui;

import path.wiser.mobile.R;
import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
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

	private String	tag	= "WiserActivity";

	/**
	 * Base constructor
	 * 
	 * @param tag
	 */
	public WiserActivity( String tag )
	{
		this.tag = tag;
	}

	/**
	 * Creates a customized Toast with the wiser logo.
	 * 
	 * @param message
	 */
	protected void showMessage( String message )
	{
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate( R.layout.custom_toast_layout, (ViewGroup) findViewById( R.id.custom_toast_layout_root ) );

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
	 * Convieniece class that helps improve readability of the code.
	 */
	protected class ClearTextView implements OnTouchListener
	{

		@Override
		public boolean onTouch( View v, MotionEvent event )
		{
			if (event.getAction() == MotionEvent.ACTION_MOVE)
			{
				( (TextView) v ).setText( "" );
			}
			return false;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onResume()
	 */
	public void onResume()
	{
		super.onResume();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 * Convient way to output tag for error and information reporting when
	 * writing to Losg.
	 */
	@Override
	public String toString()
	{
		return tag;
	}

}
