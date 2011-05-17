/**
 * 
 */
package path.wiser.mobile.ui;

import path.wiser.mobile.R;
import android.app.Activity;
import android.text.Editable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author anisbet
 *         This class provides customized toast messages without the need to do
 *         a lot of coding. Extend this class if you need to alert the user of
 *         something important. It also allows for LAF for WiserPathActivities.
 * 
 */
public abstract class WiserActivityHelper extends Activity
{

	private String	tag	= "WiserActivityHelper";

	/**
	 * Base constructor
	 * 
	 * @param tag
	 */
	public WiserActivityHelper( String tag )
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
	protected class ClearTextView implements OnTouchListener, OnFocusChangeListener
	{
		private Editable	currentText	= null;

		/*
		 * Erases the text in the box ready for new text.
		 * (non-Javadoc)
		 * 
		 * @see android.view.View.OnTouchListener#onTouch(android.view.View,
		 * android.view.MotionEvent)
		 */
		@Override
		public boolean onTouch( View v, MotionEvent event )
		{
			if (event.getAction() == MotionEvent.ACTION_MOVE)
			{
				// get the current text if nothing is entered
				this.currentText = ( (TextView) v ).getEditableText();
				( (TextView) v ).setText( "" );
			}
			return false;
		}

		/*
		 * If the user changes focus and doesn't enter anything this will
		 * replace the text s/he had there.
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.view.View.OnFocusChangeListener#onFocusChange(android.view
		 * .View, boolean)
		 */
		@Override
		public void onFocusChange( View v, boolean hasFocus )
		{
			if (this.currentText != null)
			{
				Editable textNow = ( (TextView) v ).getEditableText();
				System.out.print( "action fired" );
				System.out.println( "'" + textNow.toString() + "'" );
				System.out.println( this.currentText.toString() );
				if (textNow == null || textNow.toString().equals( "" ))
				{
					( (TextView) v ).setText( this.currentText.toString() );
				}
			}
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
