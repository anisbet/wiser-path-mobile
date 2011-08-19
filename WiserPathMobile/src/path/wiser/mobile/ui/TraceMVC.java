/**
 * 
 */
package path.wiser.mobile.ui;

import path.wiser.mobile.R;
import path.wiser.mobile.geo.Trace;
import path.wiser.mobile.util.ModelViewController;
import path.wiser.mobile.util.Tags;
import android.util.Log;
import android.widget.TextView;

/**
 * Updates the display of a trace, or alternately changes the trace
 * if signaled to do so.
 * 
 * @author andrew nisbet
 * 
 */
public class TraceMVC implements ModelViewController
{

	private TraceActivity	activity;
	private Trace			trace;
	private boolean			isFirstTime			= true;
	private boolean			isFreshTextEntry	= true;

	/**
	 * @param activity
	 * @param trace
	 */
	public TraceMVC( TraceActivity activity, Trace trace )
	{
		this.activity = activity;
		this.trace = trace;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see path.wiser.mobile.util.ModelViewController#update()
	 */
	@Override
	public void update()
	{
		// first handle the title and description and tags
		if (this.trace != null)
		{
			// take the values from the trace and write them to screen
			if (this.isFreshTextEntry)
			{
				updateTextViewFields();
			}
			// now set the image now display the updated data.
			updateTripDisplay();
		}
		else
		{
			displayOnce( "Unable to update a null Trace." );
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see path.wiser.mobile.util.ModelViewController#change()
	 */
	@Override
	public void change()
	{
		if (this.trace != null)
		{
			// change the data in the model which occurs when the user changes text only
			// because the model data doesn't come from the view like it does with Points of interest.
			this.isFreshTextEntry = true; // next update() will trigger a redraw.
			TextView textView = (TextView) activity.findViewById( R.id.Trace_Title );
			trace.setTitle( textView.getText().toString() );
			textView = (TextView) activity.findViewById( R.id.Trace_Blog );
			trace.setDescription( textView.getText().toString() );
			textView = (TextView) activity.findViewById( R.id.Trace_Tag );
			Tags tags = new Tags( textView.getText().toString() );
			trace.setTags( tags );
		}
		else
		{
			Log.e( this.getClass().getName(), "Unable to update a null Trace." );
		}
	}

	/**
	 * Updates the text fields which doesn't need to be done as often as the
	 * trip computer display.
	 */
	private void updateTextViewFields()
	{
		this.isFreshTextEntry = false;
		TextView textView = (TextView) activity.findViewById( R.id.Trace_Title );
		textView.setText( trace.getTitle() );
		textView = (TextView) activity.findViewById( R.id.Trace_Blog );
		textView.setText( trace.getDescription() );
		textView = (TextView) activity.findViewById( R.id.Trace_Tag );
		Tags tag = trace.getTags();
		textView.setText( tag.toString() );
	}

	/**
	 * This method just updates the trip display which would be nice to optionally update
	 * rather than update all the
	 */
	private void updateTripDisplay()
	{
		TextView textView = (TextView) activity.findViewById( R.id.Speed );
		textView.setText( trace.getSpeed() );
		textView = (TextView) activity.findViewById( R.id.Distance );
		textView.setText( trace.getDistance() );
		textView = (TextView) activity.findViewById( R.id.Time );
		textView.setText( trace.getEllapseTime() );
		textView = (TextView) activity.findViewById( R.id.Pace );
		textView.setText( trace.getPace() );
		textView = (TextView) activity.findViewById( R.id.Direction );
		textView.setText( trace.getDirection() );
		textView = (TextView) activity.findViewById( R.id.Latitude );
		textView.setText( trace.getLatitude() );
		textView = (TextView) activity.findViewById( R.id.Longitude );
		textView.setText( trace.getLongtitude() );
	}

	/**
	 * This prevents spurious messaging for every update call.
	 * 
	 * @param message message to display
	 */
	private void displayOnce( String message )
	{
		if (this.isFirstTime)
		{
			this.isFirstTime = false;
			Log.e( this.getClass().getName(), message );
		}
	}

}
