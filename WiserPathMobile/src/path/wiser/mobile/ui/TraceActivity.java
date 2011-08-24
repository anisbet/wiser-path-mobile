package path.wiser.mobile.ui;

import path.wiser.mobile.R;
import path.wiser.mobile.geo.GPS;
import path.wiser.mobile.geo.POI;
import path.wiser.mobile.geo.Trace;
import path.wiser.mobile.services.HTTPService;
import path.wiser.mobile.util.PoiList;
import path.wiser.mobile.util.Selectable;
import android.location.Location;
import android.os.Bundle;
import android.text.Html;
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
				// TODO start the trace activity and register it as running so POIs and Incidents. This
				// should be done as a service so that it continues to run if the app is eventually minimized.
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
	protected boolean previous()
	{
		Trace currentTrace = (Trace) this.traces.getCurrent();
		if (currentTrace.isRunning())
		{
			currentTrace.stop();
		}
		TraceMVC mvc = new TraceMVC( this, currentTrace );
		// write data from UI to blog
		mvc.change();
		currentTrace = (Trace) this.traces.previous();
		mvc = new TraceMVC( this, currentTrace );
		// update the ui to the new blog
		mvc.update();
		// if this is a new blog you may need to add a location so check for GPS and if none start it.
		if (currentTrace.needsLocation())
		{
			this.gps = new GPS( this ); // this will run until a location is stored when locationChange fires.
		}
		return true;
	}

	@Override
	protected boolean upload()
	{
		boolean result = true;
		Trace currentTrace = (Trace) this.traces.getCurrent();
		if (currentTrace.isRunning())
		{
			currentTrace.stop();
		}
		TraceMVC mvc = new TraceMVC( this, currentTrace );
		// sync the data from the UI to the blog
		mvc.change();
		// Make sure the currentBlog is ready to go.
		if (currentTrace.isValid())
		{
			// get the HTTPService for posting data.
			HTTPService service = HTTPService.getInstance();
			service.uploadTrace( currentTrace );
			// upload the currentBlog.
			if (currentTrace.isUploaded())
			{
				text = String.format( res.getString( R.string.poi_blog_post_success_msg ) );
			}
			else
			{
				text = String.format( res.getString( R.string.poi_blog_post_fail_msg ) );
				result = false;
			}
		}
		else
		{
			text = String.format( res.getString( R.string.poi_blog_post_invalid_msg ) );
			result = false;
		}
		msg = Html.fromHtml( text );
		showMessage( msg );
		// get the next Blog after deletion.
		if (result == true)
		{
			currentTrace = (Trace) this.traces.deleteCurrent();
		}
		mvc = new TraceMVC( this, currentTrace );
		mvc.update();
		return result;
	}

	@Override
	protected boolean delete()
	{
		Trace currentTrace = (Trace) this.traces.getCurrent();
		if (currentTrace.isRunning())
		{
			currentTrace.stop();
		}
		currentTrace = (Trace) this.traces.deleteCurrent();
		TraceMVC mvc = new TraceMVC( this, currentTrace );
		mvc.update();
		return true;
	}

	@Override
	protected boolean save()
	{
		Trace currentTrace = (Trace) this.traces.getCurrent();
		if (currentTrace.isRunning())
		{
			currentTrace.stop();
		}
		TraceMVC mvc = new TraceMVC( this, currentTrace );
		// sync the data from the UI to the blog
		mvc.change();

		if (this.traces.serialize())
		{
			text = String.format( res.getString( R.string.poi_trace_save_success_msg ) );
		}
		else
		{
			text = String.format( res.getString( R.string.poi_trace_save_not_success_msg ) );
			return false;
		}
		return true;
	}

	@Override
	protected boolean next()
	{
		// TODO This method will have to stop the current trace and then load the next.
		Trace currentTrace = (Trace) this.traces.getCurrent();
		if (currentTrace.isRunning())
		{
			currentTrace.stop();
		}
		TraceMVC mvc = new TraceMVC( this, currentTrace );
		// write data from UI to Trace
		mvc.change();
		currentTrace = (Trace) this.traces.next();
		mvc = new TraceMVC( this, currentTrace );
		// update the ui to the new Trace
		mvc.update();
		// if this is a new blog you may need to add a location so check for GPS and if none start it.
		if (currentTrace.needsLocation())
		{
			this.gps = new GPS( this ); // this will run until a location is stored when locationChange fires.
		}
		return true;
	}

	@Override
	public void onLocationChanged( Location location )
	{
		// triggers an update to the display
		Trace currentTrace = (Trace) this.traces.getCurrent();
		if (currentTrace.isRunning())
		{
			currentTrace.setLocation( location );
			TraceMVC mvc = new TraceMVC( this, currentTrace );
			mvc.update();
		}
	}
}