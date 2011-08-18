package path.wiser.mobile.ui;

import path.wiser.mobile.R;
import path.wiser.mobile.geo.GPS;
import path.wiser.mobile.geo.POI;
import path.wiser.mobile.geo.Trace;
import path.wiser.mobile.util.PoiList;
import path.wiser.mobile.util.Selectable;
import path.wiser.mobile.util.TraceMVC;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * @author anisbet
 *         This class represents the trace screen.
 */
public class TraceActivity extends Selectable
{
	protected PoiList	traces	= null;
	protected GPS		gps		= null;

	public TraceActivity()
	{
		super( "TraceActivity" );
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		setContentView( R.layout.trace_tab );

		TextView textView = (TextView) findViewById( R.id.Trace_Blog );
		textView.setOnTouchListener( new ClearTextView() );
		textView = (TextView) findViewById( R.id.Trace_Title );
		textView.setOnTouchListener( new ClearTextView() );
		textView = (TextView) findViewById( R.id.Trace_Tag );
		textView.setOnTouchListener( new ClearTextView() );

		this.gps = new GPS( this );
		// create the container for many blogs
		this.traces = new PoiList( POI.Type.TRACE );
		Trace currentTrace = null;
		// TODO trouble shoot this in the morning.
		if (this.traces.deserialize()) // there was no blog to deserialize.
		{
			currentTrace = (Trace) traces.getCurrent();
			TraceMVC mvc = new TraceMVC( this, currentTrace );
			mvc.update();
		}

		// now set up the button that will start and stop the trace.
		// Invoke the camera when the button is clicked.
		ToggleButton startStopButton = (ToggleButton) findViewById( R.id.Trace_StartStop );
		startStopButton.setOnClickListener( new OnClickListener()
		{
			public void onClick( View touchedView )
			{
				// Intent cameraIntent = new Intent( android.provider.MediaStore.ACTION_IMAGE_CAPTURE );
				// startActivityForResult( cameraIntent, CAMERA_PIC_REQUEST );
				// TODO start the trace activity and register it as running so POIs and Incidents.
			}
		} );
	}

	@Override
	public boolean onCreateOptionsMenu( Menu menu )
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate( R.menu.navigation, menu );
		return true;
	}

	@Override
	public boolean onOptionsItemSelected( MenuItem item )
	{
		// Handle item selection
		switch (item.getItemId())
		{
		case R.id.Previous:
			return previous();
		case R.id.Upload:
			return upload();
		case R.id.Delete:
			return delete();
		case R.id.Save:
			return save();
		case R.id.Next:
			return next();
		default:
			return super.onOptionsItemSelected( item );
		}
	}

	@Override
	public void onPause()
	{
		// TODO Save current state to the database when the user selects moves
		// away from this screens
		super.onPause();

	}

	@Override
	protected boolean previous()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean upload()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean delete()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean save()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean next()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLocationChanged( Location location )
	{
		// TODO Auto-generated method stub

	}
}